package teamcool.tradego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by lguti on 7/31/16.
 */
public class URISliderView extends BaseSliderView {

    private String mUrl;
    private File mFile;
    private int mRes;
    Bitmap bitmap;
    Bitmap takenImage;
    RequestCreator rq = null;
    private ImageLoadListener mLoadListener = null;

    public URISliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        //Something is up with this bindEventAndShow
        bindEventAndShow(v, target);
        return v;
    }

    //Shouldn't override because this has never been created before.
    //Converts string back into a BitMap so I can override the method.
    @Override
    public BaseSliderView image(String url){
        if(mFile != null || mRes != 0){
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mUrl = url;

        bitmap = decodeBase64(mUrl);
        takenImage = Bitmap.createScaledBitmap(bitmap, 400, 250, true);
        return this;
    }

    @Override
    protected void bindEventAndShow(final View v, ImageView targetImageView){
        if(mUrl!=null) {
            targetImageView.setImageBitmap(takenImage);
        }
        v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }




}