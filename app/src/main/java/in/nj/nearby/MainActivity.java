package in.nj.nearby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.nj.nearby.views.LocationActivity;
import in.nj.nearby.views.OffersListActivity;

public class MainActivity extends AppCompatActivity {

    Button nearBy ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nearBy = findViewById(R.id.nearBy);
        nearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OffersListActivity.class));
            }
        });
    }
}
