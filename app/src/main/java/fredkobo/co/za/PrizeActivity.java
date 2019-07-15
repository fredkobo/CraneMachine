package fredkobo.co.za;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PrizeActivity extends AppCompatActivity {

    ImageView boxTop;
    ImageView boxBottom;
    ImageView ebCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize);

        boxTop = findViewById(R.id.box_top);
        boxBottom = findViewById(R.id.box_bottom);
        ebCoin = findViewById(R.id.coin_0);
        RelativeLayout coinLayout = findViewById(R.id.coin_layout);

        boxTop.animate().rotation(-30);
        boxBottom.animate().rotation(30);
        boxTop.setTranslationY(boxTop.getY()-200);

        for(int i=0; i<coinLayout.getChildCount(); i++){
            View view = coinLayout.getChildAt(i);
            view.animate().translationX(view.getX() + 700f).start();
            view.animate().translationY(view.getY() -500f);
        }
    }
}
