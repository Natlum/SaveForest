package es.jjsr.saveforest;

import android.content.ContentResolver;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by José Juan Sosa Rodríguez on 04/03/2018.
 */
@RunWith(AndroidJUnit4.class)
public class DeleteRecordTest {

    private final int POSITION = 0;

    @Rule
    public ActivityTestRule<HistoryAdviceActivity> mHistoryAdviceActivityTestRule =
            new ActivityTestRule<HistoryAdviceActivity>(HistoryAdviceActivity.class);

    @Test
    public void updateRecord() throws Exception{
        ContentResolver resolver = mHistoryAdviceActivityTestRule.getActivity().getContentResolver();
        ArrayList<Advice> advices = AdviceProvider.readAllRecord(resolver);
        int numAdvices = advices.size();

        onView(withId(R.id.recycler_view_items)).perform(RecyclerViewActions.actionOnItemAtPosition(POSITION, longClick()));
        pauseTestFor(2000);
        onView(withId(R.id.menu_delete)).perform(click());
        pauseTestFor(2000);
        validateRecord(resolver, numAdvices);
        pauseTestFor(5000);

    }

    private void validateRecord(ContentResolver resolver, int oldNumAdvices) throws Exception{

        ArrayList<Advice> advices = AdviceProvider.readAllRecord(resolver);

        if (advices.size() == oldNumAdvices){
            throw new Exception("Registro no eliminado");
        }

        Log.i("Delete_Test", "Registro eliminado");
    }

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
