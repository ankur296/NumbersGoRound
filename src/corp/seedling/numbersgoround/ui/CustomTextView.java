package corp.seedling.numbersgoround.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CustomTextView
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

	public CustomTextView(Context paramContext) 
	{
		super(paramContext);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(20);
		setTextColor(Color.WHITE);//ffff0000 -red
		setGravity(Gravity.CENTER);
	}

	public CustomTextView(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(20);
		setTextColor(Color.WHITE);
		setGravity(Gravity.CENTER);
	}

	public CustomTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
		if (FONT_NAME == null) {
			FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		}
		setTypeface(FONT_NAME);
		setTextSize(20);
		setTextColor(Color.WHITE);
		setGravity(Gravity.CENTER);
	}
}
