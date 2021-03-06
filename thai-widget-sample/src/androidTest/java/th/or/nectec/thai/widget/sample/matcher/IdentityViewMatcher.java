/*
 * Copyright (c) 2016 NECTEC
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
 *
 */

package th.or.nectec.thai.widget.sample.matcher;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.EditText;
import nectec.thai.identity.Identity;
import nectec.thai.widget.identity.IdentityView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class IdentityViewMatcher {

    public static Matcher<View> withIdentity(final Identity identityMatcher) {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with pretty text: " + identityMatcher.prettyPrint());
            }

            @Override
            protected boolean matchesSafely(EditText editText) {
                return editText instanceof IdentityView && identityMatcher.equals(((IdentityView) editText).getIdentity());
            }
        };
    }
}
