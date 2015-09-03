package com.joggingtrackerapp.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.server.DeleteTime;
import com.joggingtrackerapp.utils.Utils;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class TimesAdapter extends BaseAdapter {
    private Context context;
    private static ArrayList<Time> allTimes;
    private boolean stopAnimation = false;

    public TimesAdapter (Context c, ArrayList<Time> allTimes) {
        context = c;
        this.allTimes = allTimes;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run () {
                stopAnimation = true;
            }
        }, 2000);
    }


    @Override
    public int getCount () {
        return allTimes.size();
    }

    @Override
    public Object getItem (int position) {
        return allTimes.get(position);
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View holder = mInflater.inflate(R.layout.listview_time, null);

        final TextView time = (TextView) holder.findViewById(R.id.time), date = (TextView) holder.findViewById(R.id.date), distance = (TextView) holder.findViewById(R.id.distance), speed = (TextView) holder.findViewById(R.id.speed);

        final Time currentTime = allTimes.get(position);

        final LinearLayout mainLayout = (LinearLayout) holder.findViewById(R.id.mainLayout);

        final ImageView edit = (ImageView) holder.findViewById(R.id.edit);

        final ImageView delete = (ImageView) holder.findViewById(R.id.delete);
        // Fill Fields

        time.setText(currentTime.getTime());
        date.setText(Utils.formattedDate(context, currentTime.getDate()));
        distance.setText(currentTime.getDistance());
        speed.setText(currentTime.getSpeed());

        // Listeners
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                ObjectAnimator moveHolderAnimationX;
                if (!currentTime.isOptionsVisible()) {
                    moveHolderAnimationX = ObjectAnimator.ofFloat(
                            mainLayout, "translationX", 0, 110);
                    moveHolderAnimationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart (Animator animation) {
                            ObjectAnimator.ofFloat(edit, "alpha", 1f)
                                    .setDuration(200).start();
                            ObjectAnimator.ofFloat(delete, "alpha", 1f)
                                    .setDuration(200).start();
                        }

                        @Override
                        public void onAnimationEnd (Animator animation) {
                            edit.setVisibility(View.VISIBLE);
                            delete.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel (Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat (Animator animation) {

                        }
                    });

                } else {
                    moveHolderAnimationX = ObjectAnimator.ofFloat(
                            mainLayout, "translationX", 110, 0);
                    moveHolderAnimationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart (Animator animation) {
                            ObjectAnimator.ofFloat(edit, "alpha", 0f)
                                    .setDuration(300).start();
                            ObjectAnimator.ofFloat(delete, "alpha", 0f)
                                    .setDuration(300).start();
                        }

                        @Override
                        public void onAnimationEnd (Animator animation) {
                            edit.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel (Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat (Animator animation) {

                        }
                    });

                }
                moveHolderAnimationX.setDuration(300);
                moveHolderAnimationX.start();
                currentTime.setOptionsVisible(!currentTime.isOptionsVisible());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setIconAttribute(android.R.attr.alertDialogIcon)
                        .setTitle("Are you sure?")
                        .setMessage("This record will be deleted.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                new DeleteTime(context, position).execute(currentTime.getId());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        // Animation
        if (!stopAnimation) {
            ObjectAnimator addHolderAnimationY = ObjectAnimator.ofFloat(
                    holder, "translationY", Utils.getScreenDiem(context)[1], 0);
            addHolderAnimationY.setDuration(800);

            ObjectAnimator addHolderAnimationX = ObjectAnimator.ofFloat(
                    holder, "translationX", (position % 2 == 0) ? Utils.getScreenDiem(context)[0]
                            : -Utils.getScreenDiem(context)[0], 0);
            addHolderAnimationX.setDuration(800);

            AnimatorSet animset = new AnimatorSet();

            animset.play(addHolderAnimationY).with(addHolderAnimationX);

            animset.start();
        }
        return holder;
    }
}
