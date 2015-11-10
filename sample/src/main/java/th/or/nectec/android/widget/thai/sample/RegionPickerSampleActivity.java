/*
 * Copyright 2015 NECTEC
 * National Electronics and Computer Technology Center, Thailand
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

package th.or.nectec.android.widget.thai.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import th.or.nectec.android.widget.thai.addresspicker.AddressPicker;
import th.or.nectec.android.widget.thai.addresspicker.AppCompatAddressPicker;
public class RegionPickerSampleActivity extends AppCompatActivity {

    TextView textView;
    AppCompatAddressPicker appCompatAddressPicker;
    AddressPicker addressPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_picker);

        textView = (TextView) findViewById(R.id.text_debug);
        appCompatAddressPicker = (AppCompatAddressPicker) findViewById(R.id.address_view);

        addressPicker = (AddressPicker) findViewById(R.id.address_view_2);
        addressPicker.setAddressCode("141604");
    }

}
