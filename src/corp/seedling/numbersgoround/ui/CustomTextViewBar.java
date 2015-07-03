package corp.seedling.numbersgoround.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class CustomTextViewBar
  extends TextView
{
  private static Typeface FONT_NAME;
  
  public CustomTextViewBar(Context paramContext) 
  {
    super(paramContext);
    if (FONT_NAME == null) {
    	FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "Montserrat-Bold.ttf");
		
//        FONT_NAME = Typeface.createFromAsset(paramContext.getResources().getAssets(), "custom1.otf");
      }
    setTypeface(FONT_NAME);
    setTextSize(20);
    setTextColor(Color.WHITE);//ffff0000 -red
    setGravity(Gravity.CENTER);
  }
  
  public CustomTextViewBar(Context paramContext, AttributeSet paramAttributeSet)
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
  
  public CustomTextViewBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
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
