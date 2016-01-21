/*
 * Copyright © 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.android.widget.thai.address;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import th.or.nectec.android.widget.thai.OnAddressChangedListener;
import th.or.nectec.android.widget.thai.R;
import th.or.nectec.domain.thai.address.*;
import th.or.nectec.entity.thai.*;

import java.util.List;
import java.util.Stack;

public class AddressPickerDialog extends Dialog implements AddressPopup, AdapterView.OnItemClickListener {

    Stack<AddressEntity> addressStack = new Stack<>();
    private TextView statusInfoView;
    private TextView titleView;
    private ListView listView;
    private ProvinceRepository provinceRepository;
    private DistrictRepository districtRepository;
    private SubdistrictRepository subdistrictRepository;
    private OnAddressChangedListener onAddressChangedListener;
    private AddressAdapter addressAdapter;


    public AddressPickerDialog(Context context, OnAddressChangedListener onAddressChangedListener) {
        this(context, 0, onAddressChangedListener);
    }

    public AddressPickerDialog(Context context, int themeResId, OnAddressChangedListener onAddressChangedListener) {
        super(context, themeResId);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.onAddressChangedListener = onAddressChangedListener;

        provinceRepository = InMemoryJsonProvinceRepository.getInstance(context);
        districtRepository = InMemoryJsonDistrictRepository.getInstance(context);
        subdistrictRepository = InMemoryJsonSubdistrictRepository.getInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);

        setContentView(R.layout.fragment_address_list_picker);
        titleView = (TextView) findViewById(R.id.title_text);
        statusInfoView = (TextView) findViewById(R.id.status_info);
        listView = (ListView) findViewById(R.id.picker_list);
        listView.setOnItemClickListener(this);


        switchPage();
    }

    @Override
    public void onBackPressed() {
        if (addressStack.empty()) {
            dismiss();
            notifyAddressCancel();
            return;
        }
        addressStack.pop();
        switchPage();

    }

    private void notifyAddressCancel() {
        onAddressChangedListener.onAddressCanceled();
    }

    private void switchPage() {
        if (addressStack.empty()) {
            showProvinceList();
            return;
        }
        AddressEntity choosedEntity = addressStack.peek();
        if (choosedEntity instanceof Province) {
            showDistrictList(choosedEntity.getCode());
        } else if (choosedEntity instanceof District) {
            showSubDistrictList(choosedEntity.getCode());
        } else if (choosedEntity instanceof Subdistrict) {
            notifyAddressChange(choosedEntity.getCode());
            dismiss();
        }
    }

    private void showSubDistrictList(String code) {
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(makeTitle());
        statusInfoView.setText(R.string.choose_subdistrict);

        List<Subdistrict> subDistricts = subdistrictRepository.findByDistrictCode(code);
        setListAdapter(new AddressAdapter<>(getContext(), subDistricts));
    }

    private String makeTitle() {
        return addressStack.peek().getName();
    }

    private void notifyAddressChange(String code) {
        AddressController controller = new AddressController(
                subdistrictRepository, districtRepository, provinceRepository, new AddressPresenter() {
            @Override
            public void displayAddressInfo(Address address) {
                onAddressChangedListener.onAddressChanged(address);
            }

            @Override
            public void alertAddressNotFound() {

            }
        });
        controller.showByAddressCode(code);
    }

    @Override
    public void show(Address area) {
        addressStack.push(area.getProvince());
        addressStack.push(area.getDistrict());
        show();
    }

    private void showProvinceList() {
        titleView.setVisibility(View.GONE);
        statusInfoView.setText(R.string.choose_province);

        List<Province> provinces = provinceRepository.find();
        setListAdapter(new AddressAdapter<>(getContext(), provinces));
    }

    @Override
    public void show(String addressCode) {
        Address address = new Address();
        address.setProvince(provinceRepository.findByProvinceCode(addressCode.substring(0, 2)));
        address.setDistrict(districtRepository.findByDistrictCode(addressCode.substring(0, 4)));
        show(address);
    }

    private void setListAdapter(AddressAdapter addressAdapter) {
        this.addressAdapter = addressAdapter;
        listView.setAdapter(addressAdapter);
    }

    @Override
    public void setOnAddressChangedListener(OnAddressChangedListener onAddressChangedListener) {
        this.onAddressChangedListener = onAddressChangedListener;
    }

    private void showDistrictList(String code) {
        titleView.setText(addressStack.peek().getName());
        titleView.setVisibility(View.VISIBLE);
        statusInfoView.setText(R.string.choose_district);

        List<District> districts = districtRepository.findByProvinceCode(code);
        setListAdapter(new AddressAdapter<>(getContext(), districts));
    }

    public AddressPickerDialog setRepository(ProvinceRepository province) {
        this.provinceRepository = province;
        return this;
    }

    public AddressPickerDialog setRepository(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
        return this;
    }

    public AddressPickerDialog setRepository(SubdistrictRepository subdistrictRepository) {
        this.subdistrictRepository = subdistrictRepository;
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        AddressEntity choosedEntity = addressAdapter.getItem(position);
        addressStack.push(choosedEntity);
        switchPage();
    }


}
