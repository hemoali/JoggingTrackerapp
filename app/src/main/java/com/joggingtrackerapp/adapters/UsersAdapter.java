package com.joggingtrackerapp.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joggingtrackerapp.Objects.User;
import com.joggingtrackerapp.R;
import com.joggingtrackerapp.server.DeleteUser;
import com.joggingtrackerapp.server.EditUser;
import com.joggingtrackerapp.ui.MainActivityForUsers_AdminsView;
import com.joggingtrackerapp.utils.Checks;
import com.joggingtrackerapp.utils.Utils;

import java.util.ArrayList;

/**
 * Created by ibrahimradwan on 9/3/15.
 */
public class UsersAdapter extends BaseAdapter {
    private Context context;
    private static ArrayList<User> allUsers;
    private AlertDialog editUserDialog;
    private int translationXDP = 90;

    public UsersAdapter (Context c, ArrayList<User> allUsers) {
        context = c;
        this.allUsers = allUsers;
    }


    @Override
    public int getCount () {
        return allUsers.size();
    }

    @Override
    public Object getItem (int position) {
        return allUsers.get(position);
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View holder = mInflater.inflate(R.layout.listview_user, null);

        final TextView id = (TextView) holder.findViewById(R.id.id), email = (TextView) holder.findViewById(R.id.email);

        final User currentUser = allUsers.get(position);

        final LinearLayout mainLayout = (LinearLayout) holder.findViewById(R.id.mainLayout);

        final ImageView edit = (ImageView) holder.findViewById(R.id.edit);

        final ImageView delete = (ImageView) holder.findViewById(R.id.delete);

        final ImageView user_times = (ImageView) holder.findViewById(R.id.user_tmies);
        // Fill Fields

        id.setText(currentUser.getId());
        email.setText(currentUser.getEmail());

        if (currentUser.getLevel().equals("1")) {
            id.setTextColor(Color.GREEN);
        } else if (currentUser.getLevel().equals("0")) {
            id.setTextColor(Color.RED);
        }
        if (Utils.checkLevel(context) == 0) {
            translationXDP = 135;
        }
        // Listeners
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                ObjectAnimator moveHolderAnimationX;
                if (!currentUser.isOptionsVisible()) {
                    moveHolderAnimationX = ObjectAnimator.ofFloat(
                            mainLayout, "translationX", 0, Utils.dpToPx(context, translationXDP));
                    moveHolderAnimationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart (Animator animation) {
                            ObjectAnimator.ofFloat(edit, "alpha", 1f)
                                    .setDuration(200).start();
                            ObjectAnimator.ofFloat(delete, "alpha", 1f)
                                    .setDuration(200).start();
                            if (Utils.checkLevel(context) == 0) {
                                ObjectAnimator.ofFloat(user_times, "alpha", 1f)
                                        .setDuration(200).start();
                            }
                        }

                        @Override
                        public void onAnimationEnd (Animator animation) {
                            edit.setVisibility(View.VISIBLE);
                            delete.setVisibility(View.VISIBLE);
                            if (Utils.checkLevel(context) == 0) {
                                user_times.setVisibility(View.VISIBLE);
                            }
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
                            mainLayout, "translationX", Utils.dpToPx(context, translationXDP), 0);
                    moveHolderAnimationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart (Animator animation) {
                            ObjectAnimator.ofFloat(edit, "alpha", 0f)
                                    .setDuration(300).start();
                            ObjectAnimator.ofFloat(delete, "alpha", 0f)
                                    .setDuration(300).start();
                            if (Utils.checkLevel(context) == 0) {
                                ObjectAnimator.ofFloat(user_times, "alpha", 0f)
                                        .setDuration(300).start();
                            }
                        }

                        @Override
                        public void onAnimationEnd (Animator animation) {
                            edit.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                            if (Utils.checkLevel(context) == 0) {
                                user_times.setVisibility(View.GONE);
                            }
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
                currentUser.setOptionsVisible(!currentUser.isOptionsVisible());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                holder.performClick();
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setIconAttribute(android.R.attr.alertDialogIcon)
                        .setTitle("Are you sure?")
                        .setMessage("This user will be deleted.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                new DeleteUser(context, position).execute(currentUser.getId());
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
                View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_add_user, null);
                AlertDialog.Builder editUserDialogBuilder = new AlertDialog.Builder(context);
                editUserDialogBuilder.setView(view);
                editUserDialogBuilder.setCancelable(true);

                // Listeners
                Button editItem = (Button) view.findViewById(R.id.addUser);
                editItem.setText("Edit");
                Button cancel = (Button) view.findViewById(R.id.cancel);
                final EditText emailET = (EditText) view.findViewById(R.id.email);
                final EditText passET = (EditText) view.findViewById(R.id.pass);
                final EditText pass2ET = (EditText) view.findViewById(R.id.pass2);
                TextView keep_empty = (TextView) view.findViewById(R.id.keep_empty);
                final Spinner levelSP = (Spinner) view.findViewById(R.id.level);
                keep_empty.setVisibility(View.VISIBLE);
                levelSP.setVisibility((Utils.checkLevel(context) == 0) ? View.VISIBLE : View.GONE);
                levelSP.setSelection(Integer.parseInt(currentUser.getLevel()));

                emailET.setText(currentUser.getEmail());

                editItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        String emailStr = emailET.getText().toString().trim();
                        String passStr = passET.getText().toString().trim();
                        String pass2Str = pass2ET.getText().toString().trim();
                        String levelStr = String.valueOf(levelSP.getSelectedItemPosition());
                        if (levelSP.getVisibility() == View.GONE) {
                            levelStr = currentUser.getLevel();
                        }
                        if (emailStr.equals("") || emailStr == null) {
                            Toast.makeText(context, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!Checks.isEmailValid(emailStr)) {
                                Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!pass2Str.trim().equals(passStr.trim())) {
                                    Toast.makeText(context, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!levelStr.equals("0") && !levelStr.equals("1") && !levelStr.equals("2")) {
                                        Toast.makeText(context, "Invalid Level", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (passStr.trim().length() > 0 && (passStr.trim().length() < 8)) {
                                            Toast.makeText(context, "Password Must Be 8 or More Characters", Toast.LENGTH_SHORT).show();
                                        } else {
                                            new EditUser(context, editUserDialog, levelStr).execute(emailStr, passStr, currentUser.getId(), String.valueOf(position));
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        editUserDialog.dismiss();
                    }
                });
                editUserDialog = editUserDialogBuilder.show();
            }
        });
        user_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                holder.performClick();
                Intent i = new Intent(context, MainActivityForUsers_AdminsView.class);
                i.putExtra("userID", currentUser.getId());
                context.startActivity(i);
            }
        });

        return holder;
    }
}
