package corp.seedling.numbersgoround.ui;

import corp.seedling.numbersgoround.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CustomTextViewHelp
extends TextView
{
	private static Typeface FONT_NAME;
	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	String originalValue;

	public CustomTextViewHelp(Context paramContext) 
	{
		super(paramContext);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(20);
		setTextColor(getResources().getColor(R.color.game_dark_green));//ffff0000 -red
		setGravity(Gravity.CENTER);
	}

	public CustomTextViewHelp(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(20);
		setTextColor(getResources().getColor(R.color.game_dark_green));
		setGravity(Gravity.CENTER);
	}

	public CustomTextViewHelp(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(20);
		setTextColor(getResources().getColor(R.color.game_dark_green));
		setGravity(Gravity.CENTER);
	}
}
