package net.gautampulla.shoppinglist;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

//
// Extend the TextView class so that we have a TextView with a custom
// font. We want to use a script (handwriting) font in the list-view
// of the shopping list, and this font is packaged in our app as a
// ttf asset file. Custom font files must be programmatically loaded,
// they cannot be loaded at design time in the XML-designer.
//
public class ScriptTextView extends TextView {

	public ScriptTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ScriptTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScriptTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"Snake.ttf");
			setTypeface(tf);
		}
	}

}
