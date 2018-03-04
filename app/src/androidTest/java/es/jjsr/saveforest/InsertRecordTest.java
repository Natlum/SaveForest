package es.jjsr.saveforest;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;
import es.jjsr.saveforest.dto.Advice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by José Juan Sosa Rodríguez on 04/03/2018.
 */
@RunWith(AndroidJUnit4.class)
public class InsertRecordTest {

    @Rule
    public ActivityTestRule<StartActivity> mStartActivityTestRule =
            new ActivityTestRule<StartActivity>(StartActivity.class);
    @Rule
    public ActivityTestRule<FormStepBAvtivity> mformStepBAvtivityTestRule =
            new ActivityTestRule<FormStepBAvtivity>(FormStepBAvtivity.class, false, false);

    @Test
    public void insertCallRecord() throws Exception{
        onView(withId(R.id.textName)).perform(replaceText("Nombre Test"));
        onView(withId(R.id.checkBoxCallMe))
                .perform(click())
                .check(matches(isChecked()));
        pauseTestFor(2500);
        onView(withId(R.id.next)).perform(click());

        Intent i = new Intent();
        i.putExtra("name", "Nombre Test");
        mformStepBAvtivityTestRule.launchActivity(i);

        introduceDataAnsSave();
    }

    private void introduceDataAnsSave() throws Exception{
        //onView(withId(R.id.SpinerCountryStepB)).perform(click());
        onView(withId(R.id.editTextPhoneNumber)).perform(replaceText("999888777"));
        onView(withId(R.id.editTextDescriptionStepB)).perform(replaceText("Texto de prueba"));

        ContentResolver resolver = mformStepBAvtivityTestRule.getActivity().getContentResolver();

        pauseTestFor(2500);
        onView(withId(R.id.floatingButtonSendStepB)).perform(click());
        pauseTestFor(5000);
        Log.i("Insert_Test", "Se ha añadido el registro en el Content Provider");
        validateRecord(resolver);
    }

    private void validateRecord(ContentResolver resolver) throws Exception{
        boolean found = false;

        ArrayList<Advice> advices = AdviceProvider.readAllRecord(resolver);

        for (Advice advice: advices) {
            if (advice.getName().equals("Nombre Test")){
                found = true;
                Log.i("Insert_Test", "Se ha verificado el registro en el Content Provider");
            }
        }

        if (!found){
            throw new Exception("Registro no verificado");
        }

    }

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
