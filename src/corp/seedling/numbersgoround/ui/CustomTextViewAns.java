package corp.seedling.numbersgoround.ui;

import corp.seedling.numbersgoround.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CustomTextViewAns
extends TextView
{
	private static Typeface FONT_NAME;

	public CustomTextViewAns(Context paramContext) 
	{
		super(paramContext);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(25);
		setTextColor(Color.RED);//ffff0000 -red
		setGravity(Gravity.CENTER);
	}

	public CustomTextViewAns(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(35);
		setTextColor(paramContext.getResources().getColor(R.color.game_dark_green) );
		setGravity(Gravity.CENTER);
	}

	public CustomTextViewAns(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(25);
		setTextColor(Color.RED);
		setGravity(Gravity.CENTER);
	}
}
