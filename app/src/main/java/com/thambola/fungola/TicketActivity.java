package com.thambola.fungola;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TicketActivity extends Activity {
    private Context activity;
    ArrayList<TextView> myTextViewList = new ArrayList<>();
    ArrayList<String> myTextViewListvalues = new ArrayList<>();

    RelativeLayout relativeLayoutmain;
    TableLayout tablelayout;
    private boolean booled;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.tickets);
       // MobileAds.initialize(this, "ca-app-pub-4121966223103572~2093106848");
        
        Random random = new Random();

        ((RelativeLayout) findViewById(R.id.backgroundtkt)).setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

        relativeLayoutmain=findViewById(R.id.relativeLayoutmain);
        //tablelayout=findViewById(R.id.tablelayout);


        /*((Button) findViewById(R.id.backbtn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
               // TicketActivity.this.createAndShowAlertDialog();
                finish();
                Intent intent=new Intent(TicketActivity.this,TicketActivity.class);
                startActivity(intent);
            }
        });*/


        if(booled)
        {
            for (int i = 0; i <2; i++) {

               // TableRow row= new TableRow(this);
                TableRow row = (TableRow) findViewById(R.id.tableRow1);
              //  TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
              //  row.setLayoutParams(lp);
                CheckBox checkBox = new CheckBox(this);
                TextView tv = new TextView(this);
               // addBtn = new ImageButton(this);
               // addBtn.setImageResource(R.drawable.add);
               // minusBtn = new ImageButton(this);
               // minusBtn.setImageResource(R.drawable.minus);
                TextView qty = new TextView(this);
                checkBox.setText("hello");
                qty.setText("10");
                row.addView(checkBox);
               // row.addView(minusBtn);
                row.addView(qty);
               // row.addView(addBtn);
                tablelayout.addView(row,i);
            }
        }
        else
        {
          /*  for(int numbtab=0;numbtab<2;numbtab++)
            {*/

                TableLayout tablelayout=new TableLayout(this);

                TableRow tableRow = (TableRow) findViewById(R.id.tableRow1);
                for (int i = 0; i < tableRow.getChildCount(); i++) {
                    if (tableRow.getChildAt(i) instanceof TextView) {
                        this.myTextViewList.add((TextView) tableRow.getChildAt(i));
                    }
                    myTextViewListvalues.add("0");
                }
                TableRow tableRow2 = (TableRow) findViewById(R.id.tableRow2);
                for (int i2 = 0; i2 < tableRow2.getChildCount(); i2++) {
                    if (tableRow2.getChildAt(i2) instanceof TextView) {
                        this.myTextViewList.add((TextView) tableRow2.getChildAt(i2));
                    }
                    myTextViewListvalues.add("0");
                }
                TableRow tableRow3 = (TableRow) findViewById(R.id.tableRow3);
                for (int i3 = 0; i3 < tableRow3.getChildCount(); i3++) {
                    if (tableRow3.getChildAt(i3) instanceof TextView) {
                        this.myTextViewList.add((TextView) tableRow3.getChildAt(i3));
                    }
                    myTextViewListvalues.add("0");
                }
                List asList = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(5)});
                Collections.shuffle(asList);
                int i4 = 0;
                int i5 = 0;
                while (i4 < 9) {
                    ArrayList randomNumbers = getRandomNumbers(Integer.valueOf(i4 == 0 ? 1 : i4 * 10), Integer.valueOf((i4 * 10) + 9), (Integer) asList.get(i4));
                    int i6 = i5;
                    for (int i7 = 0; i7 < randomNumbers.size(); i7++) {
                        if (i4 == 0) {
                            if (((Integer) asList.get(i4)).intValue() == 4) {
                                ((TextView) this.myTextViewList.get((i7 * 9) + 18)).setText(((Integer) randomNumbers.get(i7)).toString());

                                myTextViewListvalues.set(((i7 * 9) + 18),""+randomNumbers.get(i7).toString());


                                System.out.println("rrrrrrrrrrrr 111111111::::  "+((i7 * 9) + 18)+" (Integer) asList.get(i4)).intValue(): "+((Integer) asList.get(i4)).intValue());

                            } else if (((Integer) asList.get(i4)).intValue() == 5) {
                                ((TextView) this.myTextViewList.get((i7 * 9) + 9)).setText(((Integer) randomNumbers.get(i7)).toString());
                                System.out.println("rrrrrrrrrrrr 222222222222:::::::  "+((i7 * 9) + 9)+" (Integer) asList.get(i4)).intValue(): "+((Integer) asList.get(i4)).intValue());

                                myTextViewListvalues.set(((i7 * 9) + 9),""+randomNumbers.get(i7).toString());
                            } else {
                                ((TextView) this.myTextViewList.get(i7 * 9)).setText(((Integer) randomNumbers.get(i7)).toString());
                                System.out.println("rrrrrrrrrrrr 33333333 :::: "+((i7 * 9))+" (Integer) asList.get(i4)).intValue(): "+((Integer) asList.get(i4)).intValue());
                                myTextViewListvalues.set(((i7 * 9)),""+randomNumbers.get(i7).toString());
                            }
                        }
                        else if (((Integer) asList.get(i4)).intValue() == 4) {
                            ((TextView) this.myTextViewList.get((i7 * 9) + i4 + 18)).setText(((Integer) randomNumbers.get(i7)).toString());
                            myTextViewListvalues.set(((i7 * 9) + i4 + 18),""+randomNumbers.get(i7).toString());
                            System.out.println("rrrrrrrrrrrr 111111111 111111111 :::::: "+((i7 * 9)+ i4  + 18)+" (Integer) asList.get(i4)).intValue(): "+((Integer) asList.get(i4)).intValue());

                        } else if (((Integer) asList.get(i4)).intValue() == 5) {
                            ((TextView) this.myTextViewList.get((i7 * 9) + i4 + 9)).setText(((Integer) randomNumbers.get(i7)).toString());

                            myTextViewListvalues.set(((i7 * 9) + i4 + 9),""+randomNumbers.get(i7).toString());
                            System.out.println("rrrrrrrrrrrr 222222222222 2222222222:::: "+((i7 * 9)+ i4 + 9)+" (Integer) asList.get(i4)).intValue(): "+((Integer) asList.get(i4)).intValue());
                        } else {
                            ((TextView) this.myTextViewList.get((i7 * 9) + i4)).setText(((Integer) randomNumbers.get(i7)).toString());
                            myTextViewListvalues.set(((i7 * 9) + i4 ),""+randomNumbers.get(i7).toString());

                            System.out.println("rrrrrrrrrrrr 33333333 33333333333:::: "+((i7 * 9)+ i4)+" (Integer) asList.get(i4)).intValue(): "+((Integer) asList.get(i4)).intValue());
                        }
                        i6 += ((Integer) randomNumbers.get(i7)).intValue();

                        System.out.println("rrrrrrrrrrrrrrrrrrr i7 : "+i7+" randomNumbers.size(): "+randomNumbers.size());
                    }
                    /*TextView textView = (TextView) findViewById(R.id.tktnumber);
                    StringBuilder sb = new StringBuilder();
                    sb.append("No: ");
                    sb.append(Integer.toString(i6));
                    textView.setText(sb.toString());*/
                    commonOnClick();
                    i4++;

                    System.out.println("rrrrrrrrrrrrrrrrrrr i4 : "+i4);
                    i5 = i6;
                }


            String listString = "";

            for (String s : myTextViewListvalues)
            {
                listString += s + ",";
            }

            listString="Ticket1:"+listString;
            System.out.println("rrrrrrrrrrrrrrr final sring"+listString);

        }

    }

    /* access modifiers changed from: protected */
    public ArrayList<Integer> getRandomNumbers(Integer num, Integer num2, Integer num3) {
        if (num3.intValue() == 4) {
            num3 = Integer.valueOf(1);
        }
        if (num3.intValue() == 5) {
            num3 = Integer.valueOf(2);
        }
        ArrayList arrayList = new ArrayList();
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        for (int intValue = num.intValue(); intValue <= num2.intValue(); intValue++) {
            arrayList.add(new Integer(intValue));
        }
        Collections.shuffle(arrayList);
        for (int i = 0; i < num3.intValue(); i++) {
            arrayList2.add((Integer) arrayList.get(i));
        }
        Collections.sort(arrayList2);
        return arrayList2;
    }

    /* access modifiers changed from: protected */
    public void commonOnClick() {
        for (int i = 0; i < this.myTextViewList.size(); i++) {
            if (!((TextView) this.myTextViewList.get(i)).getText().toString().isEmpty()) {
                final int finalI = i;
                ((TextView) this.myTextViewList.get(i)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (((ColorDrawable) ((TextView) TicketActivity.this.myTextViewList.get(finalI)).getBackground()).getColor() == -1) {
                            ((TextView) TicketActivity.this.myTextViewList.get(finalI)).setBackgroundColor(-16711936);
                          //  ((TextView) TicketActivity.this.myTextViewList.get(finalI)).setBackgroundColor(Color.RED);
                        } else {
                            ((TextView) TicketActivity.this.myTextViewList.get(finalI)).setBackgroundColor(-1);
                           // ((TextView) TicketActivity.this.myTextViewList.get(finalI)).setBackgroundColor(Color.YELLOW);
                        }
                    }
                });
            }
        }
    }

    public void onBackPressed() {
        createAndShowAlertDialog();
    }

    /* access modifiers changed from: private */
    public void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit...");
        builder.setMessage("Are you sure you want exit from this?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                TicketActivity.this.finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


}
