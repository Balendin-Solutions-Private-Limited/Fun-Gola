package com.thambola.fungola;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anupamchugh.smoothcountdown.SmoothCircularProgressView;
import com.anupamchugh.smoothcountdown.SmoothProgressAnimationListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.startapp.sdk.adsbase.StartAppAd;
import com.thambola.fungola.Animation.WaveProgressBar;
import com.thambola.fungola.Model.GetGameWinners;
import com.thambola.fungola.Model.GetUsersPerGame;
import com.thambola.fungola.Model.GetWinnerPrice;
import com.thambola.fungola.Model.NotoficationMessage;
import com.thambola.fungola.Model.StartGame;
import com.thambola.fungola.Model.WinnerModels;
import com.thambola.fungola.Model.WinningDetails;
import com.thambola.fungola.Retrofit.ApiClient;
import com.thambola.fungola.Retrofit.ApiInterface;
import com.thambola.fungola.Store_DB.SharedPreference;
import com.thambola.fungola.Store_DB.SharedPreference_TapedClaim;
import com.thambola.fungola.Store_DB.SharedPreference_TapedCoins;
import com.thambola.fungola.Store_DB.Store_GameResponse;
import com.thambola.fungola.Store_DB.TicketTapedClaim;
import com.thambola.fungola.Store_DB.TicketTapedNumbers;
import com.thambola.fungola.Ticketsadapter.HeaderRecyclerViewSection;
import com.thambola.fungola.Utils.AESUtils;
import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.RijndaelCrypt;
import com.thambola.fungola.Utils.StringUtil;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;
import com.thambola.fungola.adapter.GameUsersAdapter;
import com.thambola.fungola.adapter.PreviousNumbersAdapter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thambola.fungola.Utils.Constants.roundoffloat;
import static com.thambola.fungola.Utils.Constants.roundoffloat1;

public class MyThambolaActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    private RecyclerView sectionHeader;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private HeaderRecyclerViewSection headerRecyclerViewSection;
 //   private static final int ONE_SECOND_IN_MS = 1000;
    private static final int ONE_SECOND_IN_MS = 1500;
  //  private static final int ONE_SECOND_IN_MS = 500;
    SmoothCircularProgressView smoothCircularProgressView;

    TextView ticketnumber;


    String listString = "";
    private List<List<String>> ticketnumbers=new ArrayList<>();
    private StartGame startgameresponse;
    private String encrypted;
    private int presentnumpos=0;
    private boolean animstart;
    private String randomGameNumbers;
    private List<String> listofrandomnumbers=new ArrayList<>();

    ImageView previous_numbers,game_winners,game_win_amount,game_users,speech_on_off;
    private PreviousNumbersAdapter previousNumbersAdapter;
    private TextView win_chance_f5,win_chance_l1,win_chance_l2,win_chance_l3,win_chance_fh;
    private Handler handler;
    private Runnable runnable;
    private TextView txtDay, txtHour, txtMinute, txtSecond,game_text,txtDate,txtTime;
    private RelativeLayout mainlayout1;
    LinearLayout relativeLayout,gameover_layout,game_starts_layout;
    ImageView game_view_results,ic_rate,ic_share;
    RelativeLayout mainlayout2;
    private String access_token;
    private boolean isgameend;
    //private PaintView fireworksView;
    private Gson gson;
    private TextToSpeech texttospeech;
    //private Store_GameResponse storegameResponse;

    private String isSpeechon="On";
    private boolean isgamestored;
    private SharedPreference sharedPreference;
    private SharedPreference_TapedCoins sharedPreferenceTapedCoins;
    private SharedPreference_TapedClaim sharedPreference_tapedClaim;
    public static MyThambolaActivity contextactivity;
   // boolean mToRightAnimation;
   // private ChangeBounds changeBounds;
   // private FrameLayout.LayoutParams params;
   // private float xvalue,yvalue;
   public static int width,height;
    private WaveProgressBar mWave;
    private LinearLayout previousnumbers_layout;
    private Animation anim,anim1;
 //   private WinAnimationView winAnimationView;
    private VideoView winAnimationView;
    int[] previous_layout_bgs=new int[]{R.drawable.previous_num_1,R.drawable.previous_num_2,R.drawable.previous_num_3,R.drawable.previous_num_4
            ,R.drawable.previous_num_5,R.drawable.previous_num_6,R.drawable.previous_num_7};
    private int previousnum;
    private Date GameDate;
    private String convertedTime,convertedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_thambola);

        // set screen visible always
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Tools.setSystemBarColor(this, R.color.theme_color1);


        DisplayMetrics dm=getResources().getDisplayMetrics();
         width=dm.widthPixels;
         height=dm.heightPixels;

        previous_numbers=(ImageView)findViewById(R.id.previous_numbers);
        game_winners=(ImageView)findViewById(R.id.game_winners);
        game_win_amount=(ImageView)findViewById(R.id.game_win_amount);
        game_users=(ImageView)findViewById(R.id.game_users);
        speech_on_off=(ImageView)findViewById(R.id.speech_on_off);

        txtDay = (TextView) findViewById(R.id.txtDay);
        txtHour = (TextView) findViewById(R.id.txtHour);
        txtMinute = (TextView) findViewById(R.id.txtMinute);
        txtSecond = (TextView) findViewById(R.id.txtSecond);

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);

        win_chance_f5 = (TextView) findViewById(R.id.win_chance_f5);
        win_chance_l1 = (TextView) findViewById(R.id.win_chance_l1);
        win_chance_l2 = (TextView) findViewById(R.id.win_chance_l2);
        win_chance_l3 = (TextView) findViewById(R.id.win_chance_l3);
        win_chance_fh = (TextView) findViewById(R.id.win_chance_fh);

        gameover_layout = findViewById(R.id.gameover_layout);
        game_starts_layout = findViewById(R.id.game_starts_layout);
        mainlayout1 = (RelativeLayout) findViewById(R.id.mainlayout1);
        mainlayout2 =  findViewById(R.id.mainlayout2);

        previousnumbers_layout=findViewById(R.id.previousnumbers_layout);
       // tansperenbg = (RelativeLayout) findViewById(R.id.tansperenbg);

        relativeLayout =  findViewById(R.id.relativeLayout);
        game_text=(TextView)findViewById(R.id.game_text);
        game_view_results=findViewById(R.id.game_view_results);
        ic_rate=findViewById(R.id.ic_rate);
        ic_share=findViewById(R.id.ic_share);


        winAnimationView =  findViewById(R.id.win_animation);
       // winAnimationView.setAlpha(0.5f);
        winAnimationView.setVisibility(View.INVISIBLE);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win_video);
        winAnimationView.setVideoURI(video);




        mWave = (WaveProgressBar) findViewById(R.id.wave_animation);
        mWave.setProgress(0);
        mWave.AnimateText(true, false);
       // tansperenbg.setAlpha(0.5f);

        //storesharedpreferences
        sharedPreference = new SharedPreference();
        sharedPreferenceTapedCoins=new SharedPreference_TapedCoins();
        sharedPreference_tapedClaim=new SharedPreference_TapedClaim();

        texttospeech = new TextToSpeech(this, this);
        gson = new Gson();

        presentnumpos=0;
        access_token = Util.getStringFromSP(MyThambolaActivity.this, Constants.access_token);
        startgameresponse= (StartGame) getIntent().getSerializableExtra("startgameresponse");

        if(startgameresponse.getTicketInfo()!="null")
            encrypted=startgameresponse.getTicketInfo();
        else
            encrypted=getIntent().getStringExtra("encrypted");

        isgamestored=getIntent().getBooleanExtra(""+Constants.isgamestored,false);


         anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
         anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation1);

        //  encrypted="56289494F28E10698F5720C4CBE16B1DE82991C6EDF96E10E16E901AD5A1D3E9CE37E48586242B4C346CB7772BF30EB83CF4340EB556B75166804AA3579D4A6AEE95B23518F08B980DE293B7BC4DBC36C83C8892FC54690CCA718AD14F457902AC37D78F802CE0F0B17EBD582617AC877A4A374D68B9E060CCB3B43EFD20B86D6B17C586EE5DD188BDF5808D569B99448555E01DDE3BBC4E8387FA20EF4F3B7C7DE3109DC001F232A6DA2F56FD498DB28A1A709DF84855165BEEB7DFD0BEBC9EA52FE3AA2639FBFE5EE3B810AAEFAC8CFAC219A4C6054C6F2F72501ABA5143B85E443FD8BAC7ABDC95E56FF48C356A692B5514E13CCE0582CA7B3CF2A05D037700066CF5682932CFA795DE32DBE0B925969F193FEA2D2EF0E45B1397839C1C3493694412EDA97B88D2C06C3FC8DE0428F4E9A1312A9655E78847312653FEE403DB319D72D08D1AD5BA4036D3BBA32607E37E150B38C6D52C259A4BE6BBCC95FF3F45562473E2388B822E7A24C252791CE663E2CC7A5F761070A0118A4BF58BD2E36498A0AE07DDB5A768A0ED327C4D5ED164A73F2E8D53C2EDF6A8A746F036B4849DA9411B58CBB40D8F1AE98CE41DC64EA172162C90B37B59A58B579F18C46728ADA56972DB8391E56285B43D1D848C3F51366EA9ABCB4534261A8308467591924E585573F8247D7D7969F1D89DDF89237A0B6979267284A0B6A448456DCE405B0DBB67B8F0FB296F133AD58499F3C25D4D473721C2149F38CBA4B0C5391195D662F99AD5316C0F129074F7AE0500D09A5F7C5347BF95B97B48B9A367AF4B8F89FC8AA98495DF71E566379B72711822F5C4266711F17783EC3AC98F2F73AA95239C9E10C990F16C0143210E60A2354C9682EAA1FDDB0BC34F65132335F3BDACC04C5A31A53197CB4B5BAE17F941A1E469E95C22C10EA89A995C7280B10703CA3B7053106CC66A228ADD35392A511E2B2E4DAC4020239A64C8F7D698C4221E1E2484A575EA43E85AFE66A3C886CD620D9EE325812F41B19DA6D430FFC2DD9F8287051336834ED81BE24CAE1B7329B4CB87D68D889D74789D5B0BC1C1431970B93AF4D8C08C55031D2060396AB1F217F0B096FF264AC0388501E05EF27B4FB9C614314ADCEE2CC59700CEC6F4277F2EDA1C643E6DB8B0269BEC2455D1040F92A38A7F0D00077AB710E277FD493B38A62CD06771543A4F7B501F845F3002DE14A0DE1A818F52EAFABE1153B034F07F82975482589AE8538DD2E7A5E31BFEAC74C8B250A0B55F265AEA237B0F5D770E7C7E97D02CCF7A11176113A194F4C487D7EED882290FB99503E581C73D1075D6BF0D8C80C703279578814A3C23BE03AE2B5D82888493A0BAC116C1F03C0340233D24FD9359EDC8C43B7DF262ACF9D199DDBDAFDA7BAE3E4E64836C70C50701F9CEAA1BA780D12743C5BFCC9E64B925A2C9DB80E93882F654A1B85D78C18300DD7E6D2057C829BA084B112BC5186F493EC493F6A221FF1AE42DEC297E3B48AB19731AAA191387E8ACA47F48CD6494F4B3237D281350C74EFD1C3F8A6E08A0C47B1BE5F19A38D392169F5AB4434730120D00701B80A37F87E7FA87164D1D10849EC6284CA3A9EE90245E1E28C2AC3A0E141218D60E3DB7A7995143EB81AB5A5E4F95BA76FC781531D72D6444EAB0830163467AEB4D808A116D6A441A62540DE33F320C01997673883C45BB3470258E987A03EC50C9DD81F40AC680963A6FB93E471720FA5897883CE57082AE6EB4932F6464F9BD85B84B4B4B4800DB4378209A80FD9C97CB3995776CA1482F96ACE7CB204409CAB57113783D9FE6E6FE942B4A95C78A92A64130EFE1FE4DC724A3D82597733C5CB6FFD98359A43A313E442EB04D37C97568DA16A993099E4CE416713223DBC0B226281A3C14A1F58BAB8E588913EF333C04CBF4A5B4FA9B160229F23B3FDF35E05501ECC4A9E5245CFF230CC461E2BB8E8B53B459D129385811A1F819953ABA7F4FD3E5401D81E7A95204FC04865191D46DC821D6C7C947559D58CCBCBEFE95925A7759E8477123418E9C73D8C13E14DF0C539678DF4D1CE87B3C6C3654DABBFFF5CFDB68A0163798CD41DF314C699169F9FEFFB905078942653AEC2A133AA2EF5040AD85A431CC8D045B1BC6A850BBC0BB68A8FC25B74DB21FD2BC360B5C4A75B158FDF5A76D8260B230CFB7F5D60D8DA7BF4F3417F39E0AD78416EEFF1549076D7057E88DC8EF0C3052EE68B912719204007CC1C6E49BED28B76C6E2C3A97A8A4E0C2EEE3A1E78152849B9C6CA5B91D51DFCEC3BEA249BBC692CFDB440CE62FEDD6BA8A47B1D82690A2440C3879D45D42AA0C8F7F86A1A801E26B13DF6D34C3DE3FC0C1DAA694584832E08F73AE62D85FC6D9FFB00005CA4C9D41C1DF8AFB988FA47FD93C8A9AC0E7A04BA5D785FD80DCA5E473B156D6B9FCF07B0CAE9CBD0933BE22561E7E43A1D530AFED969272A153B2B4E2B2E8C1A8912FA60DFC6D5E5EEA7B201FDE63B16244131317755AD86B34F5C7EDDD404A7B5AD746701068CE4984E62A08ECC9F192D0359459F1A10EA36A830AA4F5ACE1A835BFFE4969447C20A4CD70854E63E030BCBB1A2CEB9B04B1FB6AF3384D04B76DE59E344BF7EFF724547A9D771086141F1843F0EDFF6E7BC4736B40E7900457E02BC86DD3C7823D148869A06C5864ED23B0F36476E06FD5";

        contextactivity=MyThambolaActivity.this;

        try{
            isSpeechon=Util.getStringFromSP(MyThambolaActivity.this,Constants.IsSpeechon);
            if(isSpeechon.equals("Off"))
            {
                //  Util.saveBooleanInSP(MyThambolaActivity.this,Constants.IsSpeechon,true);
                speech_on_off.setImageResource(R.drawable.game_sound_on);
                texttospeech.stop();

            }
            else {
                //  Util.saveBooleanInSP(MyThambolaActivity.this,Constants.IsSpeechon,false);
                speech_on_off.setImageResource(R.drawable.game_sound_off);
                // speakOut(ticketnumber.getText().toString());
            }
        }
        catch (Exception e)
        {
            isSpeechon="On";
        }



        String getRandomGameNumbers = ""+startgameresponse.getTicketPurchased().getGame().getRandomGameNumbers();
      //  System.out.println("rrrrrrrrrrrrrrrrr getRandomGameNumbers: "+getRandomGameNumbers);
        try {
            RijndaelCrypt rijndaelCrypt=new RijndaelCrypt(Constants.Encryppassword);
            randomGameNumbers = rijndaelCrypt.decrypt(getRandomGameNumbers);

           //    System.out.println("rrrrrrrrrrrrrr rrr randomGameNumbers: "+randomGameNumbers);
        } catch (Exception e) {
            e.printStackTrace();

        }


        ticketnumber=findViewById(R.id.ticketnumber);

        sectionHeader = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyThambolaActivity.this);
        sectionHeader.setLayoutManager(linearLayoutManager);
        sectionHeader.setHasFixedSize(true);


        smoothCircularProgressView = (SmoothCircularProgressView) findViewById(R.id.smoothCircularProgress);

        //////////// Game time
        SimpleDateFormat dateFormatfuture = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //  GameDate = dateFormatfuture.parse("2020-05-22T21:31:00");
        try {
            GameDate = dateFormatfuture.parse(""+startgameresponse.getTicketPurchased().getGame().getStartDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String currentdate = dateFormat.format(calendar1.getTime());

        SimpleDateFormat timeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        convertedDate = timeFormatter1.format(GameDate);

        //SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
         convertedTime = sdf1.format(GameDate);


        if(currentdate.contains(convertedDate)) {
            convertedDate = "Today";
        }



        ///////// display game stats time
        countDownStart();

        final TextView button =  findViewById(R.id.button);


        smoothCircularProgressView.addAnimationListener(new SmoothProgressAnimationListener() {
            @Override
            public void onValueChanged(float value) {

                mWave.setProgress((int) value);

               // System.out.println("rrrrrrrrrrr isgameend "+isgameend+" presentnumpos "+presentnumpos);
                if(!animstart && !isgameend)
                {
                    presentnumpos=presentnumpos+1;

                    animstart=true;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd() {

                if(!isgameend)
                try{
                    animstart=false;
                    ticketnumber.setText(""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", ""));
                    mWave.setText(""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", ""));

                    anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation1);

                    for(int i=0;i<previousnumbers_layout.getChildCount();i++)
                    {
                        previousnumbers_layout.getChildAt(i).setAnimation(anim1);
                    }

                    TextView tv = new TextView(getApplicationContext());

                    tv.setText(ticketnumber.getText().toString());
                 //   tv.setBackgroundResource(R.drawable.previous_numb_1);

                  //  tv.setBackgroundResource(R.drawable.previous_num_1);
                    try {
                        tv.setBackgroundResource(previous_layout_bgs[previousnum]);
                    }
                    catch (Exception e)
                    {
                        previousnum=0;
                        tv.setBackgroundResource(previous_layout_bgs[previousnum]);
                    }
                    previousnum=previousnum+1;

                    tv.setTextColor(Color.WHITE);
                    tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    tv.setTextSize(17);
                    tv.setGravity(Gravity.CENTER);
                  //  tv.setShadowLayer(1.3f, 4.0f, 4.0f, Color.parseColor("#fdab52"));
                    tv.setShadowLayer(1.3f, 4.0f, 4.0f, Color.BLACK);

                    tv.setWidth(width/13);
                    tv.setHeight(width/13);

                    tv.startAnimation(anim);

                    previousnumbers_layout.addView(tv,0);

                    /// previousnumber animation

                    ValueAnimator animator = ValueAnimator.ofFloat(0, 1); // values from 0 to 1
                    animator.setDuration(1000); // 5 seconds duration from 0 to 1
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                    {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = ((Float) (animation.getAnimatedValue()))
                                    .floatValue();

                            button.setTranslationX((float)(width/9.6f * Math.sin(value/2* Math.PI)));
                            button.setTranslationY((float)(width/9.6f * Math.sin(value/2* Math.PI)));


                        }
                    });
                    animator.start();
                    button.animate().scaleX(1).scaleY(1).setDuration(1000).start();


                    if(isSpeechon.equals("On"))
                    speakOut(ticketnumber.getText().toString());

                    Constants.Presentnumber= ""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", "");

                    Constants.Presentnumberpos=presentnumpos;

                    if(!isgameend)
                    if(listofrandomnumbers.size()>(presentnumpos+1))
                    {
                        smoothCircularProgressView.setProgress(0);
                        smoothCircularProgressView.setStartAngle(270);
                        smoothCircularProgressView.setProgressWithAnimation(100, 5 * ONE_SECOND_IN_MS);

                    }

                     String claimmessage=Util.getStringFromSP(MyThambolaActivity.this,Constants.claimmessage);

                    NotoficationMessage notoficationMessage = gson.fromJson(claimmessage, NotoficationMessage.class);
                 //   System.out.println("rrrrrrrrrrrr claimmessage "+claimmessage);
                    if(!claimmessage.equals("null"))
                    {

                       Util.saveStringInSP(MyThambolaActivity.this,Constants.claimmessage,"null");

                        System.out.println("rrrrrrrrrrrrrrr notoficationMessage.getMessage(): "+notoficationMessage.getMessage()+"  :notoficationMessage.getWinners(): "+notoficationMessage.getWinners());



                        if(notoficationMessage.getWinners().contains("FirstFive"))
                        {
                            win_chance_f5.setVisibility(View.INVISIBLE);
                            if(notoficationMessage.getMessage().contains("FirstFive"))
                            {
                                String numberwithclaim=notoficationMessage.getMessage();
                                numberwithclaim=numberwithclaim.replace("FirstFive","First Five")+" \n \n "+ticketnumber.getText().toString();
                                System.out.println("rrrrrrrrrr FirstFive numberwithclaim "+numberwithclaim);
                                if(isSpeechon.equals("On"))
                                    speakOut(numberwithclaim);
                                //Toast.makeText(MyThambolaActivity.this, ""+notoficationMessage.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }


                         if(notoficationMessage.getWinners().contains("LineOne"))
                         {
                             win_chance_l1.setVisibility(View.INVISIBLE);
                             if(notoficationMessage.getMessage().contains("LineOne")) {
                                 String numberwithclaim = notoficationMessage.getMessage();
                                 numberwithclaim = numberwithclaim.replace("LineOne", "Line  One") + " \n \n " + ticketnumber.getText().toString();
                                 System.out.println("rrrrrrrrrr LineOne numberwithclaim " + numberwithclaim);
                                 if (isSpeechon.equals("On"))
                                     speakOut(numberwithclaim);
                             }
                         }


                        if(notoficationMessage.getWinners().contains("LineTwo"))
                        {
                            win_chance_l2.setVisibility(View.INVISIBLE);

                            if(notoficationMessage.getMessage().contains("LineTwo")) {
                                String numberwithclaim = notoficationMessage.getMessage();
                                numberwithclaim = numberwithclaim.replace("LineTwo", "Line  Two") + " \n \n " + ticketnumber.getText().toString();
                                System.out.println("rrrrrrrrrr LineTwo numberwithclaim " + numberwithclaim);
                                if (isSpeechon.equals("On"))
                                    speakOut(numberwithclaim);
                            }
                        }


                         if(notoficationMessage.getWinners().contains("LineThree"))
                         {
                             win_chance_l3.setVisibility(View.INVISIBLE);

                             if(notoficationMessage.getMessage().contains("LineThree")) {
                                 String numberwithclaim = notoficationMessage.getMessage();
                                 numberwithclaim = numberwithclaim.replace("LineThree", "Line Three") + " \n \n " + ticketnumber.getText().toString();
                                 System.out.println("rrrrrrrrrr LineThree numberwithclaim " + numberwithclaim);
                                 if (isSpeechon.equals("On"))
                                     speakOut(numberwithclaim);
                             }
                         }

                         if(notoficationMessage.getWinners().contains("FullHousee"))
                        {

                            win_chance_fh.setVisibility(View.INVISIBLE);

                            if(notoficationMessage.getMessage().contains("FullHousee")) {
                                String numberwithclaim = notoficationMessage.getMessage();
                                numberwithclaim = numberwithclaim.replace("FullHousee", "Full Housiie");
                                System.out.println("rrrrrrrrrr FullHousee numberwithclaim " + numberwithclaim);
                                if (isSpeechon.equals("On"))
                                    speakOut(numberwithclaim);
                            }

                            CloseGame();
                        }


                    }
                    if(previousNumbersAdapter!=null)
                        previousNumbersAdapter.setnumpostion(presentnumpos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("rrrrrrrrrrrr e"+e.getMessage());
                    animstart=true;
                    ticketnumber.setText("");
                }

            }
        });

        smoothCircularProgressView.setTextColor(Color.WHITE);


        new gettingtickets().execute();


        previous_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getpreviousnumbers();

            }
        });

        game_winners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getGameWinners();
            }
        });

        game_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getUsersperGame();
            }
        });

        game_win_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calulategamewiningmoney();
            }
        });

        speech_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isSpeechon.equals("On"))
                {
                    isSpeechon="Off";
                    Util.saveStringInSP(MyThambolaActivity.this,Constants.IsSpeechon,isSpeechon);
                    
                    speech_on_off.setImageResource(R.drawable.game_sound_on);
                    texttospeech.stop();

                }
                else {
                    isSpeechon="On";
                    Util.saveStringInSP(MyThambolaActivity.this,Constants.IsSpeechon,isSpeechon);
                    speech_on_off.setImageResource(R.drawable.game_sound_off);

                  //  speakOut("Line One");
                    speakOut(ticketnumber.getText().toString());
                }
            }
        });






    }



    public void CloseGame() {


        //loadNativeAd();
        previousnumbers_layout.removeAllViews();

        win_chance_fh.setVisibility(View.INVISIBLE);

        smoothCircularProgressView.setProgress(0);
        smoothCircularProgressView.setStartAngle(270);
        smoothCircularProgressView.setProgressWithAnimation(0, 5 * ONE_SECOND_IN_MS);

        smoothCircularProgressView.clearAnimation();
       // smoothCircularProgressView.setListener(null);
        isgameend=true;
        animstart=true;
        ticketnumber.setText("");

        mainlayout1.setVisibility(View.VISIBLE);
        mainlayout1.setBackgroundColor(Color.TRANSPARENT);

        game_starts_layout.setVisibility(View.GONE);
        gameover_layout.setVisibility(View.VISIBLE);
       // mainlayout2.setVisibility(View.GONE);

//        relativeLayout.setVisibility(View.GONE);
       // game_view_results.setVisibility(View.VISIBLE);
       // game_close.setVisibility(View.VISIBLE);
        game_text.setText(" GAME OVER ");

        /*//// fireworks view starts
        fireworksView.setVisibility(View.VISIBLE);
        paused=false;*/

        //winanimtion starts
        winAnimationView.setVisibility(View.VISIBLE);
        winAnimationView.resume();

        winAnimationView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                winAnimationView.start();
            }
        });

        winAnimationView.start();

        mainlayout2.setAlpha(0.5f);

        /// remove this from sharepreference
        int removepos=checkToRemoveItem(startgameresponse.getTicketPurchased().getGame().getId());
        if(removepos>=0)
        sharedPreference.removeItempos(MyThambolaActivity.this,removepos);

        //remove taped postitions from sharepreference
        ArrayList<TicketTapedNumbers> ticketTapedNumbers = sharedPreferenceTapedCoins.getTapedCoins(MyThambolaActivity.this,startgameresponse.getTicketPurchased().getGame().getId());
        sharedPreferenceTapedCoins.removeTapedGame(MyThambolaActivity.this,ticketTapedNumbers,startgameresponse.getTicketPurchased().getGame().getId());


        //remove taped claims from sharepreference
        ArrayList<TicketTapedClaim> ticketTapedClaims = sharedPreference_tapedClaim.getTapedClaim(MyThambolaActivity.this,startgameresponse.getTicketPurchased().getGame().getId());
        sharedPreference_tapedClaim.removeTapedGame(MyThambolaActivity.this,ticketTapedClaims,startgameresponse.getTicketPurchased().getGame().getId());



        ic_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        ic_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        game_view_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //interstitialAddisplay();
                StartAppAd.showAd(getApplicationContext());
                Intent intent=new Intent(MyThambolaActivity.this,View_Results.class);
                intent.putExtra("startgameresponse",startgameresponse);
                intent.putExtra("presentnumpos",presentnumpos);
                intent.putExtra("listofrandomnumbers", (Serializable) listofrandomnumbers);
                intent.putExtra("historylayout","false");

                startActivity(intent);
                finish();
            }
        });
    }

    public int checkToRemoveItem(String gameid) {

      //  System.out.println("rrrrrrrrrrrrr gameid "+gameid);
        int check = -1;
        List<Store_GameResponse> favorites = sharedPreference.getGames(MyThambolaActivity.this);
        if (favorites != null) {
            for(int i=0;i<favorites.size();i++)
            {
             //   System.out.println("rrrrrrrrrrrrr gameid "+favorites.get(i).getStore_gameResponse().getTicketPurchased().getGame().getId());
                if (favorites.get(i).getStore_gameResponse().getTicketPurchased().getGame().getId().equals(gameid)) {
                    check = i;
                    break;
                }
            }

        }
        return check;
    }


    @Override
    public void onInit(int status) {
       // System.out.println("rrrrrrrrrrrrrr status"+status);
        if (status == TextToSpeech.SUCCESS) {

            int result = texttospeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("texttospeech", "This Language is not supported");
            } else {

                if(isSpeechon.equals("On"))
                    speakOut(ticketnumber.getText().toString());
            }

        } else {
            Log.e("texttospeech", "Initilization Failed!");
        }
    }

    private void speakOut(String speechtext) {

        texttospeech.speak(speechtext, TextToSpeech.QUEUE_FLUSH, null);
    }


    public class gettingtickets extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ticketnumbers.clear();


            String encrypted1 = ""+encrypted;
            String decrypted = "";
            try {

                decrypted = AESUtils.decrypt(encrypted1);
                //  Log.d("TEST", "decrypted:" + decrypted);

               // decrypt
                String num = decrypted;
                //  String str[] = num.split(",");
                String str[] = num.split(" ");
                List<String> al = new ArrayList<String>();
                al = Arrays.asList(str);
                for(String split1: al){
                    //  System.out.println("rrrrrrrrrrr split string "+split1);

                    String str2[] = split1.split(":");
                    List<String> al2 = new ArrayList<String>();
                    al2 = Arrays.asList(str2);
                    System.out.println("rrrrrrrrrrr split2  string "+al2.get(1));

                    String str3[] = al2.get(1).split(",");
                    List<String> al3 = new ArrayList<String>();
                    al3 = Arrays.asList(str3);

                    ticketnumbers.add(al3);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listofrandomnumbers.clear();
            StringUtil stringUtil=new StringUtil();
            String[] listofrandomnumbers_string = stringUtil.split(randomGameNumbers, ",");

            listofrandomnumbers = Arrays.asList(listofrandomnumbers_string);
            

            try {
                String encrypted = "";
                encrypted = AESUtils.encrypt(listString);


                System.out.println("rrrrrrrrrrr encrypted "+encrypted);
                // System.out.println("rrrrrrrrrrr sourceStr "+ticketsStr);

                //  Log.d("TEST", "encrypted:" + encrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }



            sectionAdapter = new SectionedRecyclerViewAdapter();
            for(int i=0;i<ticketnumbers.size();i++)
            {
                headerRecyclerViewSection = new HeaderRecyclerViewSection(MyThambolaActivity.this,"Ticket "+(i+1),i, ticketnumbers.get(i),startgameresponse,isgamestored,listofrandomnumbers);
                sectionAdapter.addSection(headerRecyclerViewSection);

            }

            sectionHeader.setAdapter(sectionAdapter);

            //// for subscribe to a topic as notifications

          //  FirebaseMessaging.getInstance().subscribeToTopic("topics-"+startgameresponse.getTicketPurchased().getGameId())
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+startgameresponse.getTicketPurchased().getGameId())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }
                            System.out.println("rrrrrrrrrr topics"+msg);
                           //  Toast.makeText(MyThambolaActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            private Date CurrentDate;

            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {

                    mainlayout1.setVisibility(View.VISIBLE);


                    //////////// server time
                    Calendar calendar1 = Calendar.getInstance();
                    //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss z");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
                    String date = dateFormat.format(calendar1.getTime());



                    //   System.out.println("rrrttttttttttttt date: "+date);

                    //////////// Game time
                    SimpleDateFormat dateFormatfuture = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    // Please here set your event date//YYYY-MM-DD
                    //  GameDate = dateFormatfuture.parse("2020-04-21T21:31:00");
                    GameDate = dateFormatfuture.parse(""+startgameresponse.getTicketPurchased().getGame().getStartDateTime());
                    //  Date futureDate = dateFormatfuture.parse(""+date);
                    // System.out.println("rrrttttttttttttt GameDate: "+GameDate);

                    SimpleDateFormat dateFormatcurrent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    //  Date currentDate = new Date();
                    CurrentDate = dateFormatcurrent.parse(""+date);

                    System.out.println("rrrrrrrrrrrrrr CurrentDate::  "+CurrentDate+" GameDate ::: "+GameDate);

                    if (!CurrentDate.after(GameDate)) {

                        txtDate.setText(""+convertedDate);
                        txtTime.setText(""+convertedTime);
                        /*long diff = GameDate.getTime() - CurrentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtDay.setText("" + String.format("%02d", days));
                        txtHour.setText("" + String.format("%02d", hours));
                        txtMinute.setText(""
                                + String.format("%02d", minutes));
                        txtSecond.setText(""
                                + String.format("%02d", seconds));*/
                    } else {
                        mainlayout2.setVisibility(View.VISIBLE);
                        mainlayout1.setVisibility(View.GONE);

                        ////////remove handler
                        handler.removeCallbacks(runnable);


                        ////store the game details for next time
                        Store_GameResponse store_gameResponse=new Store_GameResponse(encrypted,startgameresponse);
                        sharedPreference.addFavorite(MyThambolaActivity.this, store_gameResponse);

                        long newtimedone=twoDatesBetweenTime(GameDate,CurrentDate);

                        System.out.println("rrrrrrrrrrrrrr newtimedone "+newtimedone);

                        if(newtimedone>10)
                        {
                            presentnumpos= (int) (newtimedone/7);

                            System.out.println("rrrrrrrrrrrrrrrr presentnumpos "+presentnumpos+" (newtimedone/7): "+(newtimedone/7));

                            ticketnumber.setText(""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", ""));
                            mWave.setText(""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", ""));


                        for(int i=0;i<presentnumpos;i++)
                        {
                            ticketnumber.setText(""+listofrandomnumbers.get(i).replaceAll("[^0-9]", ""));
                            mWave.setText(""+listofrandomnumbers.get(i).replaceAll("[^0-9]", ""));

                            TextView tv = new TextView(getApplicationContext());

                            tv.setText(ticketnumber.getText().toString());
                            try {
                                tv.setBackgroundResource(previous_layout_bgs[previousnum]);
                            }
                            catch (Exception e)
                            {
                                previousnum=0;
                                tv.setBackgroundResource(previous_layout_bgs[previousnum]);

                            }
                            previousnum=previousnum+1;
                            //  tv.setBackgroundResource(R.drawable.gradienorange);
                            tv.setTextColor(Color.WHITE);
                            tv.setGravity(Gravity.CENTER);
                            //  tv.setShadowLayer(1.3f, 4.0f, 4.0f, Color.parseColor("#fdab52"));
                            tv.setShadowLayer(1.3f, 4.0f, 4.0f, Color.BLACK);
                            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                            tv.setTextSize(17);

                            tv.setWidth(width/13);
                            tv.setHeight(width/13);
                            //   tv.setPadding(5,5,5,5);




                            tv.startAnimation(anim);

                            previousnumbers_layout.addView(tv,0);


                        }
                            Constants.Presentnumber= ""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", "");
                            Constants.Presentnumberpos=presentnumpos;

                        }
                        else {
                            ticketnumber.setText(""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", ""));
                            mWave.setText(""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", ""));


                        TextView tv = new TextView(getApplicationContext());

                        tv.setText(ticketnumber.getText().toString());
                        try {
                            tv.setBackgroundResource(previous_layout_bgs[previousnum]);
                        }
                        catch (Exception e)
                        {

                            tv.setBackgroundResource(previous_layout_bgs[previousnum]);
                        }
                        previousnum=previousnum+1;
                      //  tv.setBackgroundResource(R.drawable.gradienorange);
                        tv.setTextColor(Color.WHITE);
                        tv.setGravity(Gravity.CENTER);
                        //  tv.setShadowLayer(1.3f, 4.0f, 4.0f, Color.parseColor("#fdab52"));
                        tv.setShadowLayer(1.3f, 4.0f, 4.0f, Color.BLACK);
                        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tv.setTextSize(17);

                        tv.setWidth(width/13);
                        tv.setHeight(width/13);
                     //   tv.setPadding(5,5,5,5);




                        tv.startAnimation(anim);

                        previousnumbers_layout.addView(tv,0);

                            Constants.Presentnumber= ""+listofrandomnumbers.get(presentnumpos).replaceAll("[^0-9]", "");
                            Constants.Presentnumberpos=presentnumpos;
                        }


                        if(isSpeechon.equals("On"))
                            speakOut(ticketnumber.getText().toString());

                        smoothCircularProgressView.setProgress(0);
                        smoothCircularProgressView.setStartAngle(270);
                        smoothCircularProgressView.setProgressWithAnimation(100, 5 * ONE_SECOND_IN_MS);


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    private void getUsersperGame() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetUsersPerGame>> call = apiService.GetUsersPerGame(startgameresponse.getTicketPurchased().getGameId(),"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GetUsersPerGame>>() {
            @Override
            public void onResponse(Call<ArrayList<GetUsersPerGame>> call, Response<ArrayList<GetUsersPerGame>> response) {



                if (response.isSuccessful()) {


                    Dialog dialog=new Dialog(MyThambolaActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.game_users);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    RecyclerView childrecyclerview = (RecyclerView) dialog.findViewById(R.id.game_users_recyclerview);



                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MyThambolaActivity.this,1);
                    childrecyclerview.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

                    ArrayList<GetUsersPerGame> getUsersPerGames=new ArrayList<>();
                    getUsersPerGames.addAll(response.body());

                    GameUsersAdapter gameUsersAdapter = new GameUsersAdapter(MyThambolaActivity.this,getUsersPerGames);
                    childrecyclerview.setAdapter(gameUsersAdapter);

                    dialog.show();



                }
                else {
                   // recyclerView.setVisibility(View.GONE);
                   // tv_bankD.setVisibility(View.VISIBLE);
                   // Util.showAlert((Activity) context, "Unable to Process Request...", false);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<GetUsersPerGame>> call, Throwable t) {

                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void getGameWinners() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetGameWinners>> call = apiService.GetGameWinners(startgameresponse.getTicketPurchased().getGameId(),"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GetGameWinners>>() {
            @Override
            public void onResponse(Call<ArrayList<GetGameWinners>> call, Response<ArrayList<GetGameWinners>> response) {


                if (response.isSuccessful()) {


                    Dialog dialog=new Dialog(MyThambolaActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.game_winners);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView winner_f5=(TextView) dialog.findViewById(R.id.winner_f5);
                    TextView winner_l1=(TextView) dialog.findViewById(R.id.winner_l1);
                    TextView winner_l2=(TextView) dialog.findViewById(R.id.winner_l2);
                    TextView winner_l3=(TextView) dialog.findViewById(R.id.winner_l3);
                    TextView winner_fh=(TextView) dialog.findViewById(R.id.winner_fh);


                    ArrayList<GetGameWinners> getGameWinners=new ArrayList<>();

                    getGameWinners.addAll(response.body());


                    winner_f5.setText(" "+isListContains("FirstFive",getGameWinners));
                    winner_l1.setText(" "+isListContains("LineOne",getGameWinners));
                    winner_l2.setText(" "+isListContains("LineTwo",getGameWinners));
                    winner_l3.setText(" "+isListContains("LineThree",getGameWinners));
                    winner_fh.setText(" "+isListContains("FullHousee",getGameWinners));

                    dialog.show();



                }
                else {
                    // recyclerView.setVisibility(View.GONE);
                    // tv_bankD.setVisibility(View.VISIBLE);
                    // Util.showAlert((Activity) context, "Unable to Process Request...", false);
                }

            }


            private String isListContains(String whichtype, ArrayList<GetGameWinners> getGameWinners) {

                for(GetGameWinners getGameWinners1: getGameWinners)
                {
                    if(getGameWinners1.getWinType().equals(whichtype))
                        return getGameWinners1.getUsername();

                }


                return "";
            }

            @Override
            public void onFailure(Call<ArrayList<GetGameWinners>> call, Throwable t) {

                Log.d("response","Getting response from server : "+t);
            }
        });


    }

    private void calulategamewiningmoney() {



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetWinnerPrice>> call = apiService.GetWinnerPrice(startgameresponse.getTicketPurchased().getGameId(),"Bearer "+access_token);
        call.enqueue(new Callback<ArrayList<GetWinnerPrice>>() {
            @Override
            public void onResponse(Call<ArrayList<GetWinnerPrice>> call, Response<ArrayList<GetWinnerPrice>> response) {

                if (response.isSuccessful()) {


                    Dialog dialog=new Dialog(MyThambolaActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.game_win_money);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();

                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView win_type_heading = dialog.findViewById(R.id.win_type_heading);

                    TextView winner_f5=(TextView) dialog.findViewById(R.id.winamount_f5);
                    TextView winner_l1=(TextView) dialog.findViewById(R.id.winamount_l1);
                    TextView winner_l2=(TextView) dialog.findViewById(R.id.winamount_l2);
                    TextView winner_l3=(TextView) dialog.findViewById(R.id.winamount_l3);
                    TextView winner_fh=(TextView) dialog.findViewById(R.id.winamount_fh);


                    TextView winner_f5_user=(TextView) dialog.findViewById(R.id.winamount_f5_user);
                    TextView winner_l1_user=(TextView) dialog.findViewById(R.id.winamount_l1_user);
                    TextView winner_l2_user=(TextView) dialog.findViewById(R.id.winamount_l2_user);
                    TextView winner_l3_user=(TextView) dialog.findViewById(R.id.winamount_l3_user);
                    TextView winner_fh_user=(TextView) dialog.findViewById(R.id.winamount_fh_user);


                    ArrayList<GetWinnerPrice> getWinnerPrices=new ArrayList<>();

                    getWinnerPrices.addAll(response.body());


                    if(startgameresponse.getTicketPurchased().getGame().getGameType().equals(Constants.gametype))
                    {
                        win_type_heading.setText("Win Amount");

                        for(int i=0;i<getWinnerPrices.size();i++)
                        {

                            try{
                                System.out.println("rrrrrrrrrr getWinnerPrices.size() "+getWinnerPrices.size()+" getWinnerPrices.get(i).getWinningDetails() "+getWinnerPrices.get(i).getWinningDetails());
                                if(getWinnerPrices.get(i).getWinningDetails()!=null || getWinnerPrices.get(i).getWinningDetails().size()<0)
                                {
                                    float totalmoney=getWinnerPrices.get(i).getGameTotalPrice();
                                    //   float totalmoney=Float.parseFloat("120");
                                    //  float admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
                                    float firstfive_per= Float.parseFloat(getWinnerPrices.get(i).getFirstFive());
                                    float lineone_per= Float.parseFloat(getWinnerPrices.get(i).getLineOne());
                                    float linetwo_per= Float.parseFloat(getWinnerPrices.get(i).getLineTwo());
                                    float linethree_per= Float.parseFloat(getWinnerPrices.get(i).getLineThree());
                                    float fullhousie_per= Float.parseFloat(getWinnerPrices.get(i).getFullHousee());


                                    //   float admincharges=totalmoney/100*admincharges_per;
                                    float firstfive=totalmoney/100*firstfive_per;
                                    float lineone=totalmoney/100*lineone_per;
                                    float linetwo=totalmoney/100*linetwo_per;
                                    float linethree=totalmoney/100*linethree_per;
                                    float fullhousie=totalmoney/100*fullhousie_per;

                                    System.out.println("rrrrrrrrrrrr totalmoney "+totalmoney);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive "+firstfive+" lineone "+lineone+" fullhousie "+fullhousie/*+" admincharges "+admincharges*/);
                                    System.out.println("rrrrrrrrrrrrrrrrrr firstfive_per "+firstfive_per+" lineone_per "+lineone_per+" fullhousie_per "+fullhousie_per/*+" admincharges_per "+admincharges_per*/);

                                    winner_f5.setText(" "+roundoffloat.format(firstfive));
                                    winner_l1.setText(" "+roundoffloat.format(lineone));
                                    winner_l2.setText(" "+roundoffloat.format(linetwo));
                                    winner_l3.setText(" "+roundoffloat.format(linethree));
                                    winner_fh.setText(" "+roundoffloat.format(fullhousie));


                                    winner_f5_user.setText(" "+isListContainswinning("FirstFive",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_l1_user.setText(" "+isListContainswinning("LineOne",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_l2_user.setText(" "+isListContainswinning("LineTwo",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_l3_user.setText(" "+isListContainswinning("LineThree",getWinnerPrices.get(i).getWinningDetails()));
                                    winner_fh_user.setText(" "+isListContainswinning("FullHousee",getWinnerPrices.get(i).getWinningDetails()));

                                }
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }
                    else {

                        win_type_heading.setText("Win Coins");
                        for(int i=0;i<getWinnerPrices.size();i++)
                        {

                            try{
                                if(getWinnerPrices.get(i).getWinningDetails()!=null || getWinnerPrices.get(i).getWinningDetails().size()<0)
                                {
                                    float totalmoney=getWinnerPrices.get(i).getGameTotalPrice();
                                    //   float totalmoney=Float.parseFloat("120");
                                    //  float admincharges_per= Float.parseFloat(startgameresponse.getTicketPurchased().getGame().getCharge());
                                    float firstfive_per= Float.parseFloat(getWinnerPrices.get(i).getFirstFive());
                                    float lineone_per= Float.parseFloat(getWinnerPrices.get(i).getLineOne());
                                    float linetwo_per= Float.parseFloat(getWinnerPrices.get(i).getLineTwo());
                                    float linethree_per= Float.parseFloat(getWinnerPrices.get(i).getLineThree());
                                    float fullhousie_per= Float.parseFloat(getWinnerPrices.get(i).getFullHousee());


                                    winner_f5.setText(" "+roundoffloat1.format(firstfive_per));
                                    winner_l1.setText(" "+roundoffloat1.format(lineone_per));
                                    winner_l2.setText(" "+roundoffloat1.format(linetwo_per));
                                    winner_l3.setText(" "+roundoffloat1.format(linethree_per));
                                    winner_fh.setText(" "+roundoffloat1.format(fullhousie_per));


                                    winner_f5_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("FirstFive",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_l1_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("LineOne",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_l2_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("LineTwo",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_l3_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("LineThree",getWinnerPrices.get(i).getWinningDetails()))));
                                    winner_fh_user.setText(" "+roundoffloat1.format(Float.parseFloat(isListContainswinning("FullHousee",getWinnerPrices.get(i).getWinningDetails()))));

                                }
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }



                    dialog.show();



                }
                else {
                    // recyclerView.setVisibility(View.GONE);
                    // tv_bankD.setVisibility(View.VISIBLE);
                    // Util.showAlert((Activity) context, "Unable to Process Request...", false);
                }

            }




            @Override
            public void onFailure(Call<ArrayList<GetWinnerPrice>> call, Throwable t) {

                Log.d("response","Getting response from server : "+t);
            }
        });

    }

    private void getpreviousnumbers() {

        Dialog dialog=new Dialog(MyThambolaActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.previous_numbers);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RecyclerView childrecyclerview = (RecyclerView) dialog.findViewById(R.id.previous_numbers_recyclerview);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyThambolaActivity.this,10);
        childrecyclerview.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView


        previousNumbersAdapter = new PreviousNumbersAdapter(MyThambolaActivity.this,90,presentnumpos,listofrandomnumbers);
        childrecyclerview.setAdapter(previousNumbersAdapter);

        dialog.show();
    }

    private boolean isListContains(String whichtype, ArrayList<WinnerModels> getGameWinners) {

        for(WinnerModels getGameWinners1: getGameWinners)
        {
            if(getGameWinners1.getWinType().equals(whichtype))
                return true;

        }


        return false;
    }

    private String isListContainswinning(String whichtype, ArrayList<WinningDetails> getGameWinners) {

        String winmoney="0";
        for(WinningDetails getGameWinners1: getGameWinners)
        {
            if(getGameWinners1.getWinType().equals(whichtype))
                return ""+getGameWinners1.getMoney();

        }


        return winmoney;
    }

    @Override
    public void onDestroy() {
        System.out.println("rrrrrrrrr onDestroy");

        //turn off the visible screen while closing the activity
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //remove all views of previous numbers
        previousnumbers_layout.removeAllViews();

        // Don't forget to shutdown texttospeech!
        if (texttospeech != null) {
            texttospeech.stop();
            texttospeech.shutdown();
        }

        ////////remove handler
        if(handler!=null)
           handler.removeCallbacks(runnable);

        presentnumpos=0;
        if(smoothCircularProgressView!=null)
        {
            animstart=true;
            isgameend=true;
            smoothCircularProgressView.setProgress(0);
            smoothCircularProgressView.setStartAngle(270);
            smoothCircularProgressView.setProgressWithAnimation(0, 0);
            smoothCircularProgressView.clearAnimation();
            smoothCircularProgressView.destroyDrawingCache();
            smoothCircularProgressView.invalidate();
            smoothCircularProgressView.setActivated(false);
            smoothCircularProgressView.setVisibility(View.GONE);
        }

        /// for adds
       /* if (adView != null) {
            adView.destroy();
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }*/
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        System.out.println("rrrrrrrrr onBackPressed");

        if(mainlayout1.getVisibility()== View.GONE && isgameend==false)
        {

            displayexitdailog();
        }
        else {

           exitparams();

            super.onBackPressed();
        }

    }

    private void exitparams() {

        previousnumbers_layout.removeAllViews();

        presentnumpos=0;
        if(smoothCircularProgressView!=null)
        {
            animstart=true;
            isgameend=true;
            smoothCircularProgressView.setProgress(0);
            smoothCircularProgressView.setStartAngle(270);
            smoothCircularProgressView.setProgressWithAnimation(0, 0);
            smoothCircularProgressView.clearAnimation();
            smoothCircularProgressView.destroyDrawingCache();
            smoothCircularProgressView.invalidate();
            smoothCircularProgressView.setActivated(false);
            smoothCircularProgressView.setVisibility(View.GONE);


        }
    }

    private void displayexitdailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyThambolaActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are You Sure!! Do you want to exit Game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        exitparams();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public long twoDatesBetweenTime(Date gameDate, Date curentDate)
    {
        // TODO Auto-generated method stub
        int day = 0;
        int hh = 0;
        int mm = 0;
        int ss = 0;
        long newminutes=0;

        Long timeDiff = curentDate.getTime() - gameDate.getTime();
        //   day = (int) TimeUnit.MILLISECONDS.toDays(timeDiff);
        //   hh = (int) (TimeUnit.MILLISECONDS.toHours(timeDiff) - TimeUnit.DAYS.toHours(day));
        mm = (int) (TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
        ss = (int) (TimeUnit.MILLISECONDS.toSeconds(timeDiff) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff)));

        //  newminutes=TimeUnit.MINUTES.toMillis(mm)+TimeUnit.SECONDS.toMillis(ss);
        newminutes= TimeUnit.MINUTES.toSeconds(mm)+ss;

        return newminutes;
         //   return hh + " hour " + mm + " min"+ss+" sec"+" newminutes "+newminutes;


    }



    @Override
    protected void onPause() {
        super.onPause();
        winAnimationView.pause();
    }


}
