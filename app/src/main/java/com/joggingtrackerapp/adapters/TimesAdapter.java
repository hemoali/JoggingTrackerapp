package com.joggingtrackerapp.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joggingtrackerapp.Objects.Time;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.server.DeleteTime;
import com.joggingtrackerapp.server.EditTime;
import com.joggingtrackerapp.utils.Utils;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class TimesAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Time> allTimes;
    private AlertDialog editTimeDialog;

    public TimesAdapter (Context c, ArrayList<Time> allTimes) {
        context = c;
        this.allTimes = allTimes;
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
                            mainLayout, "translationX", 0, Utils.dpToPx(context, 45));
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
                            mainLayout, "translationX", Utils.dpToPx(context, 45), 0);
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
                holder.performClick();
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                holder.performClick();
                View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_add_time, null);
                AlertDialog.Builder addTimeDialogBuilder = new AlertDialog.Builder(context);
                addTimeDialogBuilder.setView(view);
                addTimeDialogBuilder.setCancelable(true);

                // Listeners
                Button editItem = (Button) view.findViewById(R.id.addItem);
                editItem.setText("Edit");
                Button cancel = (Button) view.findViewById(R.id.cancel);
                final EditText time = (EditText) view.findViewById(R.id.time);
                final EditText distance = (EditText) view.findViewById(R.id.distance);
                final DatePicker date = (DatePicker) view.findViewById(R.id.date);

                time.setText(currentTime.getTime());
                distance.setText(currentTime.getDistance());
                date.init(Integer.parseInt(currentTime.getDate().substring(0, 4)),
                        Integer.parseInt(currentTime.getDate().substring(5, 7)),
                        Integer.parseInt(currentTime.getDate().substring(8, 10)), null);

                editItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        String timeStr = time.getText().toString().trim();
                        String distanceStr = distance.getText().toString().trim();
                        String dateStr = date.getYear() + "-" + String.format("%02d", date.getMonth() + 1) + "-" + String.format("%02d", date.getDayOfMonth());
                        if (timeStr.equals("") || timeStr == null || distanceStr.equals("") || distanceStr == null ||
                                dateStr.trim().equals("") || dateStr == null) {
                            Toast.makeText(context, "All Fields Are Required", Toast.LENGTH_SHORT).show();

                        } else {
                            new EditTime(context, editTimeDialog).execute(dateStr, timeStr, distanceStr, currentTime.getId(), String.valueOf(position));
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        editTimeDialog.dismiss();
                    }
                });
                editTimeDialog = addTimeDialogBuilder.show();
            }
        });


        return holder;
    }

}
