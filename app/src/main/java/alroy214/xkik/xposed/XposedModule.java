package alroy214.xkik.xposed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.Room;

import com.crossbowffs.remotepreferences.RemotePreferences;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;

import alroy214.xkik.BuildConfig;
import alroy214.xkik.R;
import alroy214.xkik.Views.GlanceLurkers;
import alroy214.xkik.Views.WaveView;
import alroy214.xkik.data_types.messageText;
import alroy214.xkik.databases.WhoRead;
import alroy214.xkik.databases.WhoReadDatabase;
import alroy214.xkik.enums.Colors;
import alroy214.xkik.enums.Message;
import alroy214.xkik.settings.Settings;
import alroy214.xkik.utilities.XposedUtils;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static alroy214.xkik.enums.Colors.COLOR_CODE_BACKGROUND;
import static alroy214.xkik.enums.Colors.COLOR_CODE_INCOMING;
import static alroy214.xkik.enums.Colors.COLOR_CODE_INNER_WAVE;
import static alroy214.xkik.enums.Colors.COLOR_CODE_OUTGOING;
import static alroy214.xkik.enums.Colors.COLOR_CODE_PRIMARY;
import static alroy214.xkik.enums.Colors.COLOR_CODE_TERTIARY;
import static alroy214.xkik.enums.Colors.COLOR_CODE_TOOLBAR;
import static alroy214.xkik.enums.Colors.COLOR_CODE_WHITE;
import static alroy214.xkik.utilities.HookKeys.kikConversationFragment;
import static alroy214.xkik.utilities.HookKeys.kikMessage;
import static alroy214.xkik.utilities.Statics.APP_DEFAULT_PREFERENCE;
import static alroy214.xkik.utilities.Statics.APP_PREFERENCE_PACKAGE;
import static alroy214.xkik.utilities.Statics.KIK_PACKAGE;
import static alroy214.xkik.utilities.Statics.WHO_READ_DIR_NAME;
import static alroy214.xkik.utilities.Statics.WHO_READ_FILE_EXTENSION;
import static alroy214.xkik.utilities.Statics.WHO_READ_FILE_NAME;
import static alroy214.xkik.utilities.XposedStatics.groupPattern;
import static alroy214.xkik.xposed.hooks.kikAbTestsFragment;
import static alroy214.xkik.xposed.hooks.kikAnonymousInterestFilterFragment;
import static alroy214.xkik.xposed.hooks.kikBubbleFrameLayout;
import static alroy214.xkik.xposed.hooks.kikChatBubbleSelectionFragment;
import static alroy214.xkik.xposed.hooks.kikChatFragment;
import static alroy214.xkik.xposed.hooks.kikEmojiStatusPickerFragment;
import static alroy214.xkik.xposed.hooks.kikFullScreenAddressbookFragment;
import static alroy214.xkik.xposed.hooks.kikGalleryWidget;
import static alroy214.xkik.xposed.hooks.kikGifWidget;
import static alroy214.xkik.xposed.hooks.kikInterestsPickerFragment;
import static alroy214.xkik.xposed.hooks.kikMakeFriendsOnboardingFragment;
import static alroy214.xkik.xposed.hooks.kikMultiselectContactsListFragment;
import static alroy214.xkik.xposed.hooks.kikPKG;
import static alroy214.xkik.xposed.hooks.kikPaidThemeMarketplaceFragment;
import static alroy214.xkik.xposed.hooks.kikRobotoTextView;
import static alroy214.xkik.xposed.hooks.kikSmileyWidget;
import static alroy214.xkik.xposed.hooks.kikStickerWidget;
import static alroy214.xkik.xposed.hooks.kikWebWidget;
import static alroy214.xkik.xposed.hooks.kikWelcomeFragment;

public class XposedModule implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {
    private Context chatContext = null;
    private Settings settings = null;
    private static DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
    private static DateFormat betterFormat = new SimpleDateFormat("dd/MM HH:mm:ss", Locale.getDefault());
    private static String modulePath;
    private String lastLurkingRespondent = "";
    private View meetView;
    private String coreId = "";
    private static SharedPreferences pref = null;
    private WhoReadDatabase whoReadDatabase;

    public Settings getSettings() {
        return settings;
    }

    public Context getChatContext() {
        return chatContext;
    }


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("XKik ZYGOTE!");
        modulePath = startupParam.modulePath;
        // This is to use the configuration from the app
        pref = new XSharedPreferences(BuildConfig.APPLICATION_ID);
        ((XSharedPreferences)pref).makeWorldReadable();
    }

    @Override
    public void handleInitPackageResources(final XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (!resParam.packageName.equals(kikPKG)) {
            return;
        }
        XModuleResources modRes = XModuleResources.createInstance(modulePath, resParam.res);

        resParam.res.setReplacement(kikPKG, "drawable", "img_navbar_logo", modRes.fwd(R.drawable.ic_xkik_logo));

        resParam.res.hookLayout(kikPKG, "layout", "activity_conversations", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                final View imageView = layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("topbar_logo", "id", kikPKG));
                imageView.setClickable(true);
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                       GlanceLurkers qc = new GlanceLurkers();
                       qc.show(((Activity) v.getContext()).getFragmentManager(), "glance_lurker");
                       return true;
                    }
                });
                /*/*TODO:DUBG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuickConfiguration qc = new QuickConfiguration();
                        qc.show(((Activity)v.getContext()).getFragmentManager(), "quick_config");
                    }
                });

                 */
                imageView.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                ImageView view = (ImageView) v;
                                //overlay is black with transparency of 0x77 (119)
                                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                                view.invalidate();
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                ImageView view = (ImageView) v;
                                //clear the overlay
                                view.getDrawable().clearColorFilter();
                                view.invalidate();
                            }
                            case MotionEvent.ACTION_CANCEL: {
                                ImageView view = (ImageView) v;
                                //clear the overlay
                                view.getDrawable().clearColorFilter();
                                view.invalidate();
                                break;
                            }
                        }

                        return false;
                    }
                });
            }});

        if (settings == null || !settings.getGraphics()) {
            return;
        }

        resParam.res.setReplacement(kikPKG, "drawable", "fab_add", modRes.fwd(R.drawable.ic_add));
        if (!settings.getDarkBg()) {
            resParam.res.setReplacement(kikPKG, "drawable", "bottom_incoming_bubble_mask", modRes.fwd(R.drawable.bottom_incoming_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "bottom_outgoing_bubble_mask", modRes.fwd(R.drawable.bottom_outgoing_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "bottom_outgoing_image_bubble_mask", modRes.fwd(R.drawable.bottom_outgoing_image_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "middle_incoming_bubble_mask", modRes.fwd(R.drawable.middle_incoming_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "middle_outgoing_bubble_mask", modRes.fwd(R.drawable.middle_outgoing_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "middle_outgoing_image_bubble_mask", modRes.fwd(R.drawable.middle_outgoing_image_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "outgoing_top_round_bubble_mask", modRes.fwd(R.drawable.outgoing_top_round_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "outgoing_top_square_bubble_mask", modRes.fwd(R.drawable.outgoing_top_square_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "single_incoming_bubble_mask", modRes.fwd(R.drawable.single_incoming_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "single_outgoing_bubble_mask", modRes.fwd(R.drawable.single_outgoing_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "single_outgoing_image_bubble_mask", modRes.fwd(R.drawable.single_outgoing_image_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "top_incoming_bubble_mask", modRes.fwd(R.drawable.top_incoming_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "top_outgoing_bubble_mask", modRes.fwd(R.drawable.top_outgoing_bubble_mask));
            resParam.res.setReplacement(kikPKG, "drawable", "top_outgoing_image_bubble_mask", modRes.fwd(R.drawable.top_outgoing_image_bubble_mask));
        }

        resParam.res.hookLayout(kikPKG, "layout", "outgoing_message_bubble", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                scrollingText((TextView) liparam.view.findViewById(liparam.res.getIdentifier("message_timestamp", "id", kikPKG)));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "kik_back_button", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                //      scrollingText((TextView) layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("title_view", "id", hooks.kikPKG)));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "chat_group_profile_view", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
                scrollingText((TextView) layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("profile_name", "id", kikPKG)));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "chat_user_profile_view", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
                scrollingText((TextView) layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("profile_name", "id", kikPKG)));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "find_people_action", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "fragment_send_to", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });


        resParam.res.hookLayout(kikPKG, "layout", "fragment_one_to_one_matching", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "fragment_one_to_one_matching_v3", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });
        resParam.res.hookLayout(kikPKG, "layout", "anonymous_match_bar", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                meetView = layoutInflatedParam.view;
                meetView.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });


        resParam.res.hookLayout(kikPKG, "layout", "fragment_user_profile", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "fragment_public_group_search", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "chat_search_view", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));

                layoutInflatedParam.view.findViewById(
                        layoutInflatedParam.res.getIdentifier("chat_search_bar","id", kikPKG))
                        .setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));

                layoutInflatedParam.view.findViewById(
                        layoutInflatedParam.res.getIdentifier("chat_search_bar_container","id", kikPKG))
                        .setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));

                layoutInflatedParam.view.findViewById(
                        layoutInflatedParam.res.getIdentifier("chat_search_back_button","id", kikPKG))
                        .setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "abc_popup_menu_header_item_layout", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });


        resParam.res.hookLayout(kikPKG, "layout", "abc_popup_menu_item_layout", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });

        resParam.res.hookLayout(kikPKG, "layout", "navbar_underline", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.setBackgroundColor(settings.getColor(COLOR_CODE_WHITE));
            }
        });


        resParam.res.hookLayout(kikPKG, "layout", "activity_chat", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.findViewById(
                        layoutInflatedParam.res.getIdentifier("scroll_to_last_read_button","id", kikPKG))
                        .getBackground().setColorFilter(settings.getColor(COLOR_CODE_WHITE), PorterDuff.Mode.MULTIPLY);

                layoutInflatedParam.view.findViewById(
                        layoutInflatedParam.res.getIdentifier("new_messages_button","id", kikPKG))
                        .getBackground().setColorFilter(settings.getColor(COLOR_CODE_WHITE), PorterDuff.Mode.MULTIPLY);
            }
        });


        resParam.res.hookLayout(kikPKG, "layout", "list_entry_conversations", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("conversation_divider_short", "id", kikPKG))
                        .setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));
                layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("conversation_divider_long", "id", kikPKG))
                        .setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));
                //    scrollingText((TextView) layoutInflatedParam.view.findViewById(layoutInflatedParam.res.getIdentifier("conversation_name", "id", hooks.kikPKG)));
            }
        });

        /*
        Replaces colors
         */
        for (final Colors c : Colors.values()) {
            if (c.getColor().startsWith("#xkik")) {
                // special conditions handled here
            } else {
                try {
                    resParam.res.setReplacement(kikPKG, "color", c.getColor(), settings.getColor(c));
                } catch (Resources.NotFoundException ex) {
                    XposedBridge.log("Skipping unknown resource " + c);
                }
            }
        }


        XposedBridge.log("Colors");

        resParam.res.setReplacement(kikPKG, "color", "kik_blue", settings.getColor(COLOR_CODE_INCOMING));
        resParam.res.setReplacement(kikPKG, "color", "blue_button_selector", settings.getColor(COLOR_CODE_INCOMING));
        resParam.res.setReplacement(kikPKG, "color", "gray_3", settings.getColor(COLOR_CODE_TERTIARY));

        /*
        Replaces string Colors
         */
        for (Message s : Message.values()) {
            try {
                resParam.res.setReplacement(kikPKG, "string", s.getString(), settings.getString(s));
            } catch (Resources.NotFoundException ex) {
                XposedBridge.log("Skipping unknown resource " + s);
            }
        }

    }

    public void scrollingText(TextView tv){
        if (settings.getScrollingtxt()) {
            tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tv.setSelected(true);
            tv.setSingleLine(true);
            tv.setMarqueeRepeatLimit(-1);
        }
    }

    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(KIK_PACKAGE)) {
            return;
        }
//
//        XposedHelpers.findAndHookMethod("com.android.server.am.ActivityManagerService",
//                loadPackageParam.classLoader,
//                "appRestrictedInBackgroundLocked",
//                int.class,
//                String.class,
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) {
//                        if (param.args[1].equals("org.meowcat.edxposed.manager")) {
//                            param.setResult(0);
//                        }
//                    }
//                });


        XposedBridge.log("XKik Started");


        settings = new Settings(pref);

         // load settings
        if (settings.getNoHook()) {
            return;
        }
        format.setTimeZone(TimeZone.getDefault()); // set timezone, to keep accurate date correct

        XposedBridge.log("XKik Loaded");
        conversationMethods(loadPackageParam);
        lurkingMethods(loadPackageParam);
        if (settings.getLongCam()) { // In order to avoid checking in each method, this is set
            //TODO!
        }


        //      final JSONObject tweaksObject = loadJSONFromAsset(getContext());

        if(settings.getGraphics()) {
            otherMethods(loadPackageParam);
        }

    }

    private void otherMethods(final  XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedBridge.log("XKik Blocker");

                /*
                Receipt blocker
                 */
        XposedHelpers.findAndHookMethod(hooks.kikRecptMgr, loadPackageParam.classLoader, "attribute", String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                String key = (String) param.args[1];
                String value = (String) param.args[2];
                if (key.equalsIgnoreCase("type")) { // receipt type
                    //XposedBridge.log("type: " + value);

                    if (value.equalsIgnoreCase("read")) { // read receipt
                        if (settings != null && settings.getNoReadReceipt()) {
                            XposedBridge.log("Blocked a read receipt");
                            param.setResult(null);
                        }
                    } else if (value.equalsIgnoreCase("is-typing")) { // typing receipt
                        if (settings != null && settings.getNoTyping()) {
                            XposedBridge.log("Blocked a typing receipt");
                            param.setResult(null);
                        }
                    }

                }
                super.beforeHookedMethod(param);
            }
        });

                /*
                Sets all incoming messages to white to fix the transparency issue
                 */


        XposedBridge.log("XKik Bubble");

        XposedBridge.hookAllConstructors(XposedHelpers.findClass(kikBubbleFrameLayout, loadPackageParam.classLoader), new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (settings.getGraphics() && !settings.getDarkBg()) {

                    //      XposedHelpers.callMethod(param.thisObject, "setBackground", new ColorDrawable(settings.getColors().get("Primary Text")));
                    //     XposedHelpers.callMethod(param.thisObject, "setBackground", new ColorDrawable(Color.WHITE));

                    //Gets the root of all incoming messages
                    View view = (View) XposedHelpers.callMethod(param.thisObject, "getRootView");

                    //Default color in the case of a default parameter or lack of settings.
                    int backgroundIncoming = Color.WHITE;
                    int backgroundOutgoing = Color.BLUE;
                    try {
                        if (settings != null) {
                            backgroundIncoming = settings.getColor(COLOR_CODE_INCOMING);
                            backgroundOutgoing = settings.getColor(COLOR_CODE_OUTGOING);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Sets the background
                    //    view.getBackground().setColorFilter(backgroundIncoming,
                    //              PorterDuff.Mode.SRC_ATOP);
                    final Object outgoing = new XposedObject(param.thisObject).get("e");
                    if (!(outgoing instanceof Boolean)) {
                        XposedBridge.log("XKik - Here we go again in Frame");
                        return;
                    }
                    if ((boolean) outgoing) {
                        if (true || settings.getOutgoing()) { //TODO: not working
                            view.setBackgroundColor(backgroundOutgoing);
                        } else {
                            return;
                        }
                    } else {
                        if (true || settings.getIncoming()) {  //TODO: not working
                            view.setBackgroundColor(backgroundIncoming);
                        } else {
                            return;
                        }
                    }
                    //In the case of transparent item, it will correct it on any change
                    //Changes may turn color into transparent, this reverts it
                    final int finalIncoming = backgroundIncoming;
                    final int finalOutgoing = backgroundOutgoing;
                    view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (right != oldRight) {
                                int color;
                                if ((boolean) outgoing) {
                                    if (true || settings.getOutgoing()) { //TODO: not working
                                        color = finalOutgoing;
                                    } else {
                                        return;
                                    }
                                } else {
                                    if (true || settings.getIncoming()) {  //TODO: not working
                                        color = finalIncoming;
                                    } else {
                                        return;
                                    }
                                }
                                //If transparent and resets it
                                if (v.getBackground() == null) {
                                    v.setBackgroundColor(color);
                                } else {
                                    v.getBackground().setColorFilter(color,
                                            PorterDuff.Mode.SRC_ATOP);
                                }
                                v.invalidate();
                            }
                        }
                    });
                }
            }
        });

        if (settings.getGraphics()) {
            XposedHelpers.findAndHookMethod(kikSmileyWidget, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikWebWidget, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikStickerWidget, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikGifWidget, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikGalleryWidget, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikAnonymousInterestFilterFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikChatBubbleSelectionFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikAbTestsFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikEmojiStatusPickerFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikFullScreenAddressbookFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikInterestsPickerFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikWelcomeFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikMakeFriendsOnboardingFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikPaidThemeMarketplaceFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikMultiselectContactsListFragment, loadPackageParam.classLoader,
                    "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ((View) param.getResult()).setBackgroundColor(settings.getColor(COLOR_CODE_BACKGROUND));
                            super.afterHookedMethod(param);
                        }
                    });

            XposedHelpers.findAndHookMethod(kikChatFragment, loadPackageParam.classLoader, "f", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("Toolbar method before hook");
                    XposedObject object = new XposedObject(param.thisObject);
                    String jid = ((Bundle) object.call("getArguments")).getString("jid");
                    XposedBridge.log("Toolbar JID: " + jid);
                    //    XposedBridge.log("Toolbar b: " + new xposedObject(new xposedObject(object.get("b")).call("a", jid)).get("b"));
                    // param.setResult(null);
                    super.beforeHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookMethod(kikChatFragment, loadPackageParam.classLoader, "k", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (settings.getTheWave()) {
                        param.setResult(null);
                    } else {
                        super.beforeHookedMethod(param);
                    }
                }
            });

            //outline
            XposedHelpers.findAndHookMethod(kikBubbleFrameLayout, loadPackageParam.classLoader, "a", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedObject object = new XposedObject(param.thisObject);
                    if (object.get("l") == null && settings.getIncoming()) {
                        object.set("l", settings.getColor(COLOR_CODE_INNER_WAVE));
                    }
                    super.beforeHookedMethod(param);
                }
            });


            XposedHelpers.findAndHookMethod(kikRobotoTextView, loadPackageParam.classLoader, "a",
                    "kik.core.themes.a.b", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (settings.getDarkBg() || !(settings.getIncoming() && settings.getOutgoing())) {
                                return;
                            }
                            param.setResult(null);
                            if (new XposedObject(param.args[0]).get("i") != null) {
                                new XposedObject(param.thisObject).call("j",
                                        Color.parseColor(new XposedObject(param.args[0]).get("i").toString()));
                            }
                            if (settings.getGradient() && new XposedObject(param.args[0]).get("i") != null) {
                                String color = new XposedObject(param.args[0]).get("i").toString();
                                XposedBridge.log("Color: " + color);
                                GradientDrawable clip;
                                if (color.toLowerCase().contains("ffffff")) {
                                    int incoming = settings.getColor(COLOR_CODE_INCOMING);
                                    if (settings.getIncoming()) {
                                        clip = new GradientDrawable(
                                                GradientDrawable.Orientation.LEFT_RIGHT,
                                                new int[]{incoming, settings.getColor(COLOR_CODE_OUTGOING)});
                                    } else {
                                        return;
                                    }
                                } else {
                                    if (settings.getOutgoing()) {
                                        int outgoing = settings.getColor(COLOR_CODE_OUTGOING);
                                        clip = new GradientDrawable(
                                                GradientDrawable.Orientation.LEFT_RIGHT,
                                                new int[]{Color.parseColor(color), outgoing});
                                    } else {
                                        return;
                                    }
                                }
                                clip.setCornerRadius(60);
                                Drawable drawable1 = clip.mutate().getCurrent();
                                new XposedObject(param.thisObject).call("setBackground", drawable1);
                            }
                        }
                    });

            XposedHelpers.findAndHookMethod(kikRobotoTextView, loadPackageParam.classLoader, "a", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    new XposedObject(param.thisObject).call("setLinkTextColor", Color.WHITE);
                    super.afterHookedMethod(param);
                }
            });
        }

                /*
                Gets the chat fragment. Currently does nothing but
                plans include adding animated backgrounds
                 */
        XposedHelpers.findAndHookMethod(kikChatFragment, loadPackageParam.classLoader, "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String group = ((Bundle) XposedHelpers.callMethod(param.thisObject, "getArguments")).getString("chatGroupJID");
                String jid = ((Bundle) XposedHelpers.callMethod(param.thisObject, "getArguments")).getString("jid");
                XposedBridge.log("Bundle Group: " + group);
                XposedBridge.log("Bundle JID: " + jid);

                final Activity kact = (Activity) XposedHelpers.callMethod(param.thisObject, "getActivity");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = kact.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(settings.getColor(COLOR_CODE_TOOLBAR));
                    window.setNavigationBarColor(settings.getColor(COLOR_CODE_TOOLBAR));
                }


                final FrameLayout fl = (FrameLayout) param.getResult();
                Bitmap bitmap;// = Bitmap.createBitmap(768, 1280, Bitmap.Config.ARGB_8888);

                ViewGroup bg = ((ViewGroup) fl.getChildAt(0));
                for (int i = 0; i < bg.getChildCount(); i++) {
                    if (bg.getChildAt(i) instanceof ViewGroup) {
                        XposedBridge.log("xkik Child At " + i + ": " + ((ViewGroup) bg.getChildAt(i)).getChildCount());
                    }
                }
                XposedBridge.log("Child: " + ((ViewGroup) bg.getChildAt(2)).getChildCount());
                ViewGroup toolbar = ((ViewGroup) bg.getChildAt(6));

                TextView titleBar = ((TextView) ((ViewGroup) ((ViewGroup) toolbar.getChildAt(0)).getChildAt(0)).getChildAt(1));


                String title = titleBar.getText().toString();
                XposedBridge.log("Title: " + (titleBar.getText().toString()));
                if (settings.getGraphics() && !settings.getDarkBg()) {
                    try {
                        if (!settings.getFileList().isEmpty()) {
                            /*File file = settings.getFileList().get(new Random().nextInt(
                                    settings.getFileList().size()));

                            if (file.exists()) {
                                FileInputStream streamIn = new FileInputStream(file);

                                bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image

                                streamIn.close();

                                fl.getChildAt(0).setBackground(new BitmapDrawable(kact.getResources(), bitmap)); //background
                            }*/
                        }
                        if (settings.getTheWave()) {
                            toolbar.setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //     fl.getChildAt(3).setBackgroundColor(Color.MAGENTA); //Includes the chat

                    //      fl.getChildAt(4).setBackgroundColor(Color.BLUE); //I think includes toolbar

                    // ((ViewGroup)bg.getChildAt(3)).getChildAt(0).setBackgroundColor(Color.parseColor("#6F73FF")); //Background
                    //       XposedBridge.log("Count: "+((ViewGroup)((ViewGroup)bg.getChildAt(3)).getChildAt(0)).getChildCount());
                    //     ((ViewGroup)((ViewGroup)bg.getChildAt(3)).getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor("#6F73FF")); //Background

/*                            ((ViewGroup) bg.getChildAt(3)).getChildAt(1).getBackground().setColorFilter(
                                    Color.parseColor("#0084b4"), PorterDuff.Mode.MULTIPLY); //"UNSEEN MESSAGES"
                                    */


                    //                bg.getChildAt(6).setBackgroundColor(Color.CYAN); //Toolbar
                    // toolbar.getChildAt(0).setBackgroundColor(Color.RED);

                    //                  titleBar.setBackgroundColor(Color.RED);
                    //     tv.setTextColor(Color.WHITE);
                    //               titleBar.setText("Pie");
                    //                      toolbar.getChildAt(1).setBackgroundColor(Color.GREEN);

                    // XposedBridge.log("Title: "+((ViewGroup)titleBar.getChildAt(0)).getChildCount());


/*
                        for(int i = 0; i<titleBar.getChildCount();i++)
                        {
                            try {
                                titleBar.getChildAt(i).setBackgroundColor(Color.MAGENTA);
                                XposedBridge.log("Child ("+i+"):"+((ViewGroup)titleBar.getChildAt(i)).getChildCount());
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }*/
                    if (settings.getTheWave()) {

                        toolbar.getChildAt(0).setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));
                        bg.setBackgroundColor(settings.getColor(COLOR_CODE_TOOLBAR));

                        //        final Activity kact = (Activity) XposedHelpers.callMethod(param.thisObject, "getActivity");
                        //     final FrameLayout fl = (FrameLayout) param.getResult();

                        //       final Bitmap finalBitmap = bitmap;
                        new Thread() {
                            @Override
                            public void run() {
                                final View v = (View) XposedHelpers.getObjectField(param.thisObject, "_messageRecyclerView");
                                final Bitmap b = Bitmap.createBitmap(768, 1280 / 2, Bitmap.Config.ARGB_8888);
                                final Canvas c = new Canvas(b);
                                int outerColor = Color.WHITE;
                                try {
                                    outerColor = getSettings().getColor(COLOR_CODE_BACKGROUND);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (!getSettings().getInnerWave()) {
                                    c.drawColor(outerColor);
                                    kact.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            v.setBackground(new BitmapDrawable(v.getResources(), b));
                                        }
                                    });
                                    return;
                                }

                                final WaveView wv = new WaveView(768, 1280 / 2);

                                int innerColor = Color.RED;

                                try {
                                    innerColor = getSettings().getColor(COLOR_CODE_INNER_WAVE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                wv.setWaveColor(innerColor, outerColor, null);
                                //   finalBitmap.copy(Bitmap.Config.ARGB_8888, true));
                                wv.setShowWave(true);
                                int time = 0;
                                while (true) {
                                    time++;
                                    if (v != null) {
                                        if (wv.getWaveShiftRatio() >= 1F) {
                                            wv.setWaveShiftRatio(0F);
                                        }

                                        wv.setAmplitudeRatio((float) Math.abs(Math.sin(.04 * time) * .005));
                                        wv.setWaveShiftRatio(wv.getWaveShiftRatio() + 0.01F);
                                        wv.draw(c);

                                        kact.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                v.setBackground(new BitmapDrawable(v.getResources(), b));
                                            }
                                        });
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }.start();
                    }
                }
            }
        });




                /*
                Manages bypass options
                 */
        XposedBridge.hookAllMethods(XposedHelpers.findClass(hooks.kikContentMessage, loadPackageParam.classLoader), "z", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(settings.getBypassSave()) param.setResult(false);
                super.afterHookedMethod(param);
            }
        });


                /*
                Manages most of the media options
                 */
        XposedBridge.hookAllMethods(XposedHelpers.findClass(hooks.kikMsgClass, loadPackageParam.classLoader), "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.getResult() != null && param.getResult().getClass().getName().equals(hooks.kikContentMessage)) {
                    if (settings.getDisableFwd()) {
                        XposedHelpers.callMethod(param.getResult(), "a", "allow-forward", "false");
                    }
                    if (settings.getDisableSave()) {
                        XposedHelpers.callMethod(param.getResult(), "a", "disallow-save", "true");
                    }
                    if (settings.getAutoLoop()) {
                        XposedHelpers.callMethod(param.getResult(), "a", "video-should-loop", "true");
                    }
                    if (settings.getAutoMute()) {
                        XposedHelpers.callMethod(param.getResult(), "a", "video-should-be-muted", "true");
                    }
                    if (settings.getAutoPlay()) {
                        XposedHelpers.callMethod(param.getResult(), "a", "video-should-autoplay", "true");
                    }

                }
                super.afterHookedMethod(param);
            }
        });
    }
    private void lurkingMethods(final  XC_LoadPackage.LoadPackageParam loadPackageParam) {
        /*
            Who's lurking feature
         */
        XposedHelpers.findAndHookMethod(hooks.KIK_RECEIPT_RECV, loadPackageParam.classLoader,
                "a"/*"parseChildElement"*/, hooks.kikReceiptParser, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                Vector msgs = (Vector) new XposedObject(param.thisObject).get("m");

                if (new XposedObject(param.thisObject).get("l").equals(500)) {
                    Object o = new XposedObject(param.thisObject).call("b");
                    if(o == null) {
                        XposedBridge.log("XKik - stuff wooop null");
                        return;
                    } else {
                        XposedBridge.log("XKik - stuff yyeeeee");
                    }
                    if(new XposedObject(o).get("c") == null) {
                        XposedBridge.log("XKik - stuff c var null");
                        return;
                    } else {
                        XposedBridge.log("XKik - stuff yyccccc");
                    }
                    String whoJID = new XposedObject(o).get("c").toString();
                    XposedBridge.log("XKik - who JID: " + whoJID);
                    XposedBridge.log("XKik - who JID UserName: " +
                            XposedUtils.userNameFromJID(chatContext, coreId, whoJID));
               /*     String from;
                    Matcher userMatcher = XposedStatics.useridPattern.matcher(whoJID);
                    if (userMatcher.find()) {
                        from = userMatcher.group(1);
                    } else {
                        return;
                    }
                    if (from == null || from.equals("warehouse@talk.kik.com")) { // avoids kik internal classes
                        return;
                    } */

                    char[] txtBuf = (char[]) XposedHelpers.getObjectField(param.args[0], "srcBuf");
                    String resp = String.valueOf(txtBuf);
                    if (lastLurkingRespondent.equals(resp)) {
                        XposedBridge.log("Repeat message - lurker already logged");
                        return;
                    }

                    lastLurkingRespondent = resp;

                    for (Object uuid : msgs) {
                        XposedBridge.log("XKik - who msgs: " +uuid + " boop " + msgs.toString());
                        XposedDatabaseUtils.addWhoRead(whoReadDatabase, whoJID, uuid.toString());
                    }

                    Matcher groupMatcher = groupPattern.matcher(resp);

                    String from = XposedUtils.userNameFromJID(chatContext, coreId, whoJID);

                    String text = (groupMatcher.find()) ? "Group Message was read by: " + from
                            + "\nIn group: " + XposedUtils.userNameFromJID(chatContext, coreId,groupMatcher.group(1)) :
                            "Message was read by: " + from;
                    File textFile = new File(chatContext.getExternalFilesDir(WHO_READ_DIR_NAME) + File.separator +
                            WHO_READ_FILE_NAME + WHO_READ_FILE_EXTENSION);
                    XposedBridge.log("XKik Text " +textFile.getAbsolutePath());
                    if (!textFile.exists()) {
                        if(textFile.createNewFile()) {
                            XposedBridge.log("Failed to create wholurking.txt file");
                        }
                    }

                    Calendar time = Calendar.getInstance();
                    time.add(Calendar.MILLISECOND, time.getTimeZone().getOffset(time.getTimeInMillis()));
                    Date date = time.getTime();

                    FileUtils.writeStringToFile(textFile, "\n ***\n"+text+"\n*** \n", Charset.defaultCharset(), true);
                    XposedUtils.prependPrefix(textFile, betterFormat.format(date) + "\n" + text + "\n*** \n");

                    XposedBridge.log(text);
                    XposedBridge.log("resp: " + resp);

                    if (settings.getLurkingToast()) {
                        XposedUtils.kikToast(chatContext,from + " saw your message!");
                    }
                }
            }
        });

        /*
        Who's lurking displayer
         */
        XposedHelpers.findAndHookMethod(hooks.kABSTRACT_MESSAGE_VIEW_MODEL, loadPackageParam.classLoader, "a",
                Long.class, Boolean.class, pref.getString(kikMessage, hooks.kikMessage), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                final Boolean on = (Boolean) param.args[1];
                if (on) {
                    XposedBridge.log("Seen - this - Var: " + param.thisObject.getClass());
                    XposedBridge.log("Seen - q - Var: " + new XposedObject(param.thisObject).call("q"));
                    final String UUID = new messageText(new XposedObject(param.thisObject).call("q")).getUUID();

                    XposedBridge.log("Seen - opened msg " + UUID);
                    if (UUID != null && !UUID.isEmpty() /*&&
                            whoReadDatabase.WhoReadDao().
                            settings.getWhoread().containsKey(UUID)*/) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<WhoRead> whoReads = whoReadDatabase.WhoReadDao().findByUuid(UUID);
                                Set<String> set = new HashSet<>();
                                for(WhoRead whos:whoReads) {
                                    set.add(XposedUtils.userNameFromJID(chatContext, coreId, whos.toString()));
                                }
                                if (!whoReads.isEmpty()) {
                                    String sby = "Seen by ";
                                    sby += StringUtils.join(set, ", ");
                                    param.setResult(param.getResult() + " - " + sby);
                                    XposedBridge.log("Seen - sby - Var: " + sby);
                                }
                                else {
                                    XposedBridge.log("Seen - no one seen");
                                }

                            }
                        });
                        thread.start();
                        thread.join();
                    }
                }

                super.afterHookedMethod(param);
            }
        });
    }

    private void conversationMethods(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedBridge.log("XKik - Kik Conversation Fragment");

                /*
                Settings button create; Hook onLongClick
                 */
        XposedHelpers.findAndHookMethod(pref.getString(kikConversationFragment, hooks.kikConversationFragment),
                loadPackageParam.classLoader, "onCreateView", LayoutInflater.class,
                ViewGroup.class, Bundle.class, new XC_MethodHook() {
                    @SuppressLint("NewApi")
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("XKik PREFS: " + pref.getString("test_key", "not_working"));

                        View menuFloat = (View) XposedHelpers.getObjectField(param.thisObject, "_floatingActionMenu");

                        if (settings.getNoMeet()) {
                            XposedUtils.setMeetViewState(meetView ,View.GONE);
                        }

                        ImageView settingsButton = (ImageView) XposedHelpers.getObjectField(param.thisObject, "_settingsButton");

                        if (menuFloat != null) {
                            chatContext = menuFloat.getContext();
                        } else if (settingsButton != null) {
                            chatContext = settingsButton.getContext();
                        } else {
                            return;
                        }
                        //TODO make into finally got context method

                        pref = new RemotePreferences(chatContext,
                                APP_PREFERENCE_PACKAGE, APP_DEFAULT_PREFERENCE);
                        settings = new Settings(pref);

                        XposedBridge.log("XKik REFRESHED - PREF_TEST: " +
                                pref.getString("test_key", "not_working"));

                        new Thread() {
                            @Override
                            public void run() {
                                if(chatContext == null) {
                                    return;
                                }
                                whoReadDatabase = Room.databaseBuilder(chatContext,
                                        WhoReadDatabase.class, "database-who-read").build();

                                XposedBridge.log("Database k: " + whoReadDatabase.WhoReadDao().getAll());
                                if(whoReadDatabase.WhoReadDao().getAll().isEmpty()) {
                                    return;
                                }
                                XposedBridge.log("Database k: " + whoReadDatabase.WhoReadDao().getAll().get(0).uuid);
                                XposedBridge.log("Database k: " + whoReadDatabase.WhoReadDao().getAll().get(0).messageTime);
                                XposedBridge.log("Database k: " + whoReadDatabase.WhoReadDao().getAll().get(0).uid);
                                XposedBridge.log("Database k: " + whoReadDatabase.WhoReadDao().getAll().get(0).toString());
                                XposedBridge.log("Database k: " + XposedUtils.userNameFromJID(chatContext, coreId, whoReadDatabase.WhoReadDao().getAll().get(0).toString()));
                                XposedBridge.log("Database k: " + StringUtils.join(whoReadDatabase.WhoReadDao().getAll().toArray(), ", "));


                            }
                        }.start();



                        chatContext.setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);

                        Window window = ((Activity) chatContext).getWindow();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.setStatusBarColor(settings.getColor(COLOR_CODE_TOOLBAR));
                            window.setNavigationBarColor(settings.getColor(COLOR_CODE_TOOLBAR));
                        }


                        if (BuildConfig.DEBUG) {
                            try {
                                String str = "eitan214_lnz@talk.kik.com";
                                XposedBridge.log("XKIK - TEST - JIDS: " +
                                        XposedUtils.userNameFromJID(chatContext, coreId, str));
                            } catch (Exception e) {
                                e.printStackTrace();
                                XposedBridge.log("XKIK - TEST - FUSK!");
                            }
                        }

                        if (settings.getGraphics()) {
                            settingsButton.getDrawable().setColorFilter(
                                    new BlendModeColorFilter(Color.parseColor("#00AA8D"),
                                    BlendMode.SRC_ATOP));

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                for (int index = 0; index < ((ViewGroup) menuFloat).getChildCount(); ++index) {
                                    View nextChild = ((ViewGroup) menuFloat).getChildAt(index);
                                    if (nextChild.getClass().getName().equals("com.github.clans.fab.Label")) {
                                        nextChild.getBackground().setColorFilter(
                                                new BlendModeColorFilter(settings.getColor(COLOR_CODE_INNER_WAVE),
                                                        BlendMode.MULTIPLY));
                                    } else if (nextChild.getBackground() != null) {
                                        nextChild.getBackground().setColorFilter(
                                                new BlendModeColorFilter(settings.getColor(COLOR_CODE_PRIMARY),
                                                BlendMode.MULTIPLY));
                                    }
                                }
                                menuFloat.setBackgroundTintList(ColorStateList.valueOf(settings.getColor(COLOR_CODE_TOOLBAR)));
                                getChatContext().setTheme(android.R.style.Theme_Material);
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                settingsButton.getDrawable().setColorFilter(
                                        new BlendModeColorFilter(Color.parseColor("#F44336"),
                                                BlendMode.SRC_ATOP));
                            }
                        }
                        settingsButton.invalidate();

                        super.afterHookedMethod(param);
                    }
                });
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass(/*pref.getString(KikCoreProvider,*/
                hooks.KikCoreProvider/*)*/, loadPackageParam.classLoader),
                "b"/*"getActiveCoreId"*/, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("XKik - Debug Core ID: " + param.getResult());
                        if(param.getResult() != null) {
                            coreId = (String) param.getResult();
                        }
                        super.afterHookedMethod(param);
                    }
                });
    }


}
