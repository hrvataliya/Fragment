package com.brainstorm.hardik.allwishas.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.brainstorm.hardik.allwishas.R;
import com.brainstorm.hardik.allwishas.fragment.ImageCategoriesFragment;
import com.brainstorm.hardik.allwishas.model.ImageData;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by user on 2/17/2017.
 */

public class ImageCategoriesAdapter extends RecyclerView.Adapter<ImageCategoriesAdapter.ViewHolder> {

    Context context;
    Fragment fragment;
    ArrayList<ImageData>imageDataArrayList = new ArrayList<ImageData>();

    public ImageCategoriesAdapter(Context context, Fragment fragment, ArrayList<ImageData> imageDataArrayList) {
        this.context = context;
        this.fragment = fragment;
        this.imageDataArrayList = imageDataArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item_categories_fragment, parent, false);

        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout download,whatsapp,facebook,twitter,share;
        ImageView image_item;

        public ViewHolder(View itemView) {
            super(itemView);

            image_item = (ImageView) itemView.findViewById(R.id.image_item);

            download = (LinearLayout) itemView.findViewById(R.id.download);
            whatsapp = (LinearLayout) itemView.findViewById(R.id.whatsapp);
            facebook = (LinearLayout) itemView.findViewById(R.id.facebook);
            twitter = (LinearLayout) itemView.findViewById(R.id.twitter);
            share = (LinearLayout) itemView.findViewById(R.id.share);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context.getApplicationContext()).load(imageDataArrayList.get(position).getData_content()).into(holder.image_item);

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DownloadFileFromURL().execute("");

            }

        });

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                share.setPackage("com.whatsapp");//package name of the app
                context.startActivity(Intent.createChooser(share, "Share Image"));*/

                PackageManager pm = getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("image/jpeg");
                    String text = "YOUR TEXT HERE";

                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    //waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    waIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                    context.startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent =  new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"add what a subject you want");
                String shareMessage="message body";
                //waIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                context.startActivity(Intent.createChooser(shareIntent,"Sharing via"));
            }
        });

    }

    private PackageManager getPackageManager() {
        return null;
    }

    @Override
    public int getItemCount() {
        return imageDataArrayList.size();
    }


    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                System.out.println("Starting download");
            }

            /**
             * Downloading file in background thread
             * */
            @Override
            protected String doInBackground(String... f_url) {
                int count;
                try {
                    String root = Environment.getExternalStorageDirectory().toString();

                    System.out.println("Downloading");
                    URL url = new URL(f_url[0]);

                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream to write file

                    OutputStream output = new FileOutputStream(root+"/downloadedfile.jpg");
                    byte data[] = new byte[1024];

                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        // writing data to file
                        output.write(data, 0, count);

                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }

                return null;
            }



            /**
             * After completing background task
             * **/
            @Override
            protected void onPostExecute(String file_url) {
                System.out.println("Downloaded");
            }
        }

}
