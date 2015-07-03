package corp.seedling.numbersgoround.ui;

import ru.biovamp.widget.CircleLayout;
import ru.biovamp.widget.CircleLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import corp.seedling.numbersgoround.R;
import corp.seedling.numbersgoround.utils.Utils;

public class HelpScreen extends Activity{

	FrameLayout frameLayout;
	CustomTextView centerTv, okay;
	CircleLayout circleLayout;

	public static final int MENU_ITEM_CIRCLE_SIZE_DP = 60;
	public static final int TOTAL_MENU_ITEMS = 7;
	boolean launched_over_play = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_screen);

		findViewsByIds();
		initUI();
		launched_over_play = getIntent().getBooleanExtra("launched_over_play", false);
		
		okay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HelpScreen.this, GameScreen.class));
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				finish();
			}
		});
	} 

	@Override
	public void onBackPressed() {
		if (launched_over_play == true)
			startActivity(new Intent(this, GameScreen.class));
		else
			startActivity(new Intent(this, StartingScreen.class));
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		finish();
		super.onBackPressed();
	}

	private void initUI() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		Utils.prepColorList();

		circleLayout =	new CircleLayout(this);
		circleLayout.setInnerRadius((displayMetrics.widthPixels / 2) + 100 - getSizeInPixels(MENU_ITEM_CIRCLE_SIZE_DP));

		for (int i = 0 ; i < TOTAL_MENU_ITEMS ; i++){

			CustomTextView child = new CustomTextView(this); 
			child.setId(i);
			child.setBackgroundResource(Utils.colorList.get(i));
			setBGforChild(child , i);
			LayoutParams layoutParams = new LayoutParams(getSizeInPixels(MENU_ITEM_CIRCLE_SIZE_DP), getSizeInPixels(MENU_ITEM_CIRCLE_SIZE_DP));
			circleLayout.addView(child, layoutParams);
		}
		frameLayout.addView(circleLayout);  
	}

	private void setBGforChild(CustomTextView textView, int i) {
		switch (i) {
		case 0:
			textView.setText("9");
			break;
		case 1:
			textView.setText("+");
			break;
		case 2:
			textView.setText("6");
			break;
		case 3:
			textView.setText("-");
			break;
		case 4:
			textView.setText("3");
			break;
		case 5:
			textView.setText("2");
			break;
		case 6:
			textView.setText("x");
			break;
		default:
			break;
		}		
	}


	@Override
	protected void onDestroy() {
		Drawable drawable = centerTv.getBackground();

		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		centerTv.setBackground(null);
		centerTv.setOnClickListener(null);
		centerTv = null;

		drawable = okay.getBackground();

		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		okay.setBackground(null);
		okay.setOnClickListener(null);
		okay = null;

		for (int i = 0 ; i < TOTAL_MENU_ITEMS ; i++){

			CustomTextView child = (CustomTextView)circleLayout.getChildAt(i);
			drawable = child.getBackground();
			if (drawable != null){
				drawable.setCallback(null);
				drawable = null;
			}
			child.setBackground(null);
			child.setOnClickListener(null);
			child = null;
		}

		super.onDestroy();
	}


	@Override
	protected void onPause() {
		super.onPause();
		GameApp.getAppInstance().stopPlaying();
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameApp.getAppInstance().resumePlaying();
	}

	private void findViewsByIds() {
		frameLayout = (FrameLayout)findViewById(R.id.lay_circle_help);
		centerTv = (CustomTextView) findViewById(R.id.tv_center_help);
		okay = (CustomTextView) findViewById(R.id.tv_okay);
	}

	public int getSizeInPixels(int dp){
		return (int) ( (dp * getResources().getDisplayMetrics().density)  + 0.5f) ; 
	}
}
