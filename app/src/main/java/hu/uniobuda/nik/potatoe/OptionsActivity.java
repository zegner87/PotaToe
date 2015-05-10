package hu.uniobuda.nik.potatoe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import hu.uniobuda.nik.potatoe.R;

public class OptionsActivity extends Activity {


    RadioGroup rbDiff;
    SharedPreferences prefs;

    @Override
    public void onBackPressed(){
        Intent newIntent = new Intent(OptionsActivity.this, MainActivity.class);
        OptionsActivity.this.startActivity(newIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        rbDiff = (RadioGroup) findViewById(R.id.rbDifficulty);


        prefs = this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);
        int diff = prefs.getInt("Difficulty", 0); //0 is the default value


        switch (diff)
        {
            case 0:
                rbDiff.check(R.id.rbEasy);
                break;
            case 1:
                rbDiff.check(R.id.bnNormal);
                break;
            case 2:
                rbDiff.check(R.id.bnHard);
                break;
            default:
                rbDiff.check(R.id.rbEasy);


        }




        Button bnOk = (Button)findViewById(R.id.bnOk);

        bnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    int diff = prefs.getInt("Difficulty", 0); //0 is the default value


                int selectedDiff =0;
                switch ( rbDiff.getCheckedRadioButtonId())
                {
                    case R.id.rbEasy:
                        selectedDiff=0;
                        break;
                    case R.id.bnNormal:
                        selectedDiff=1;
                        break;
                    case R.id.bnHard:
                        selectedDiff =2;
                        break;



                }

                prefs = OptionsActivity.this.getSharedPreferences("PotaToe", Context.MODE_PRIVATE);


                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt("Difficulty", selectedDiff);


                editor.commit();

                OptionsActivity.this.onBackPressed();

                // setResult(MainActivity.RESULT_OK);
            }
        });

        Button bnCancel = (Button)findViewById(R.id.bnCancel);

        bnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setResult(MainActivity.RESULT_CANCELED);
OptionsActivity.this.onBackPressed();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
  //      getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
