package com.ktc.wallpapermarket.adapter;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.wallpapermarket.MainActivity;
import com.ktc.wallpapermarket.R;
import com.ktc.wallpapermarket.utils.Constants;
import com.ktc.wallpapermarket.utils.SettingPreference;
import com.ktc.wallpapermarket.view.MyGridView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.ktc.wallpapermarket.utils.Constants.DEBUG;

/**
 * Created by fengjw on 2018/3/13.
 */

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<File> mList;
    private LruCache<String, Drawable> mCache;

    private ImageView img;
    private TextView title;
    private ImageView checkIv;

    private int selectItem = -1;

    private SettingPreference mPreference;

    private GridView gridview;

    public GridAdapter(Context context, List<File> files, GridView gridView){
        mContext = context;
        mList = files;
        this.mInflater = LayoutInflater.from(mContext);

        this.gridview = gridView;

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mCache = new LruCache<String, Drawable>(cacheSize){
            @Override
            protected int sizeOf(String key, Drawable value) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) value;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                return bitmap.getByteCount();
            }
        };

        mPreference = new SettingPreference(mContext);

    }

    @Override
    public int getCount() {
        if (mList == null){
            return 0;
        }else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Constants.debug("GridAdapter getView() position : " + position);
//        ViewHolder holder = null;
//        if (convertView == null){
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.gridview_item, null);
//            holder.img = (ImageView) convertView.findViewById(R.id.img);
//            holder.title = (TextView) convertView.findViewById(R.id.title);
//            holder.checkIv = (ImageView) convertView.findViewById(R.id.check_iv);
//            convertView.setTag(holder);
//        }else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        View view = mInflater.inflate(R.layout.gridview_item, null);
        img = (ImageView) view.findViewById(R.id.img);
        title = (TextView) view.findViewById(R.id.title);
        checkIv = (ImageView) view.findViewById(R.id.check_iv);

        if (((MyGridView)parent).isOnMeasure){
            return view;
        }
        String url = mList.get(position).getPath();
        Drawable tempDrawable = hasDrawableInCache(url);
        if (tempDrawable != null){
            img.setImageDrawable(tempDrawable);
        }else {
            try {
                AsyncLoadImageTask task = new AsyncLoadImageTask(img);
                task.execute(position);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        title.setText(mList.get(position).getName());

        int settingPosition = mPreference.loadSharedPreferences("settingPosition", -1);
        Constants.debug("settingPosition : " + settingPosition);
        if (settingPosition == position){
            Constants.debug("VISIBLE");
            checkIv.setVisibility(View.VISIBLE);
        }
        if (selectItem == settingPosition){
            Constants.debug("selectItem == position");
            checkIv.setImageResource(R.drawable.check_icon_highlight);
        }
        if (selectItem == position){
            getScaleAnimator(view, mScalable, position);
        }
        //
        //animOnlyOnce(view, position);

        return view;
    }

    public void setSelection(int position){
        selectItem = position;
        notifyDataSetChanged();
    }

    public void update(){

        notifyDataSetChanged();
    }

    /**
     * this method run only once
     * @param viewGroup
     * @param position
     */
    private void animOnlyOnce(View viewGroup, int position){
        Constants.debug("animOnlyOnce()");
        if (Constants.position == -2 && position == 0){
            Constants.debug("--------------------------------");
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_PARENT, 0f);
            scaleAnimation.setDuration(300);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.setFillAfter(true);
            viewGroup.startAnimation(animationSet);
        }
    }

    private Drawable hasDrawableInCache(String key){
        return mCache.get(key);
    }

    public class AsyncLoadImageTask extends AsyncTask<Integer, Void, Drawable>{

        private String url = null;
        private final WeakReference<ImageView> mImageViewWeakReference;
        private ImageView mImageView;

        public AsyncLoadImageTask(ImageView imageView){
            this.mImageView = imageView;
            mImageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Drawable doInBackground(Integer... integers) {
            Drawable drawable = null;
            if (isCancelled()){
                return null;
            }
            if (integers[0] >= mList.size()){
                this.cancel(true);
            }else {
                Constants.debug("integers[0] : " + integers[0]);
                this.url = mList.get(integers[0]).getPath();
                drawable = getBitmapFromUrl(this.url);
                Constants.mDList.add(integers[0], drawable);
                if (drawable != null){
                    mCache.put(this.url, drawable);
                }
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (isCancelled()){
                drawable = null;
            }
            if (mImageViewWeakReference != null){
                if (drawable != null && mImageView != null){
                    mImageView.setImageDrawable(drawable);
                }
            }
            super.onPostExecute(drawable);
        }
    }

    private Drawable getBitmapFromUrl(String url){
        Bitmap bitmap = null;
        Bitmap bitmapResult = null;
        File file = new File(url);
        bitmap = decodeSampleFromFilePath(file.getAbsolutePath(), 380, 380);
        bitmapResult = ThumbnailUtils.extractThumbnail(bitmap, 380, 380);
        if (bitmap != bitmapResult){
            bitmap.recycle();
        }
        if (bitmapResult == null){
            return null;
        }
        Drawable drawable = new BitmapDrawable(bitmapResult);
        return drawable;
    }

    private static Bitmap decodeSampleFromFilePath(String filePath, int reqWidth, int reqHeight){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        FileDescriptor fileDescriptor = null;
        if (fileInputStream == null){
            return null;
        }
        try {
            fileDescriptor = fileInputStream.getFD();
        }catch (Exception e){
            e.printStackTrace();
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inSampleSize = calculateRatioSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        }catch (Exception e){
            e.printStackTrace();
            options.inSampleSize += 1;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        }
        return bitmap;
    }

    private static int calculateRatioSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public final class ViewHolder{
        private ImageView img;
        private TextView title;
        private ImageView checkIv;
    }

    /**
     *
     */
    private AnimationSet mAnimationSet;
    protected boolean mScalable = true;
    protected float mScale = 1.1f;
    protected int lineNumber;
    public List<Animator> getScaleAnimator(View view, boolean isScale, int position){
        Constants.debug("getScaleAnimator()");
        List<Animator> animatorList = new ArrayList<>(2);
        if (!mScalable){
            return animatorList;
        }
        lineNumber = gridview.getNumColumns();
        Constants.debug("lineNumber : " + lineNumber);
        if (!gridview.isFocused()){
            Constants.debug("mGridView.isFocused() true");
            return null;
        }
        try {
            float scaleBefore = 1.0f;
            float scaleAfter = mScale;
            if (!isScale){
                scaleBefore = mScale;
                scaleAfter = 1.0f;
            }
            AnimationSet animationSet = new AnimationSet(true);
            if (mAnimationSet != null && mAnimationSet != animationSet){
                mAnimationSet.setFillAfter(false);
                mAnimationSet.cancel();
            }
            ScaleAnimation scaleAnimation = null;
            position = position - gridview.getFirstVisiblePosition();
            Constants.debug("position : " + position);

            switch (position){
                case 0:
                    Constants.debug("0");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 1:
                    Constants.debug("1");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 2:
                    Constants.debug("2");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 3:
                    Constants.debug("3");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    break;
                case 4:
                    Constants.debug("4");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 5:
                    Constants.debug("5");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 6:
                    Constants.debug("6");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0f);
                    break;
                case 7:
                    Constants.debug("7");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    break;
                case 8:
                    Constants.debug("8");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                case 9:
                    Constants.debug("9");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                case 10:
                    Constants.debug("10");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                case 11:
                    Constants.debug("11");
                    scaleAnimation = new ScaleAnimation(scaleBefore, scaleAfter, scaleBefore, scaleAfter,
                            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    break;
                default:
                    break;
            }

            scaleAnimation.setDuration(300);
            animationSet.addAnimation(scaleAnimation);
            animationSet.setFillAfter(true);
            view.startAnimation(animationSet);
            mAnimationSet = animationSet;
        }catch (Exception e){
            e.printStackTrace();
        }
        return animatorList;
    }

}
