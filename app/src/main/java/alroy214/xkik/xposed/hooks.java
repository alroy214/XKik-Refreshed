package alroy214.xkik.xposed;

/**
 * Hooks all kept in one place to make changes easier
 */
/*SHOULD ONLY BE USED AS DEFAULT VALUES*/
public class hooks {
    private static final String kikFragment = "kik.android.chat.fragment";
    private static final String kikWidget = "kik.android.widget";
    public static final String DATATYPE_MSG_TEXT = "kik.core.datatypes.messageExtensions.k";
    static final String DATATYPE_MSG_CONTENT = "kik.core.datatypes.messageExtensions.ContentMessage";
    static final String kikRecptMgr = "kik.core.net.kxml2.io.KXmlSerializer";
    static final String kikMessage = "kik.core.datatypes.aa";//*Message*/";
    static final String kikContentMessage = "kik.core.datatypes.a.b";//.messageExtensions.ContentMessage";
    static final String kikDatabaseMessage = "kik.core.datatypes.Message";
    static final String kikNetLoggingReader = "kik.android.net.LoggingBufferedReader";
    static final String kikDeviceUtils = "kik.android.util.DeviceUtils";
    static final String kikDateDisplayManager = "kik.android.util.bs";
    static final String KIK_RECEIPT_RECV = "kik.core.net.b.e";//.incoming.IncomingReceiptMessage";
    static final String kikSmileyManager = "com.kik.android.b.j";
    static final String smileyView = "com.kik.android.b.c";
    static final String kikSmileyObj = "com.kik.android.b.f";
    static final String kikActivityInit = "kik.android.util.r";
    static final String kikCameraTimer = "kik.android.c.d$5";
    static final String kikCircleBar = "kik.android.chat.view.CameraIconBarViewImpl";
    static final String kikMsgClass = "kik.android.internal.platform.PlatformHelper";
    static final String kikGifApi = "kik.android.gifs.api.g";
    static final String kikGifSearchRating = "kik.android.gifs.api.GifApiProvider.GifSearchRating";
    static final String kikBubbleFrameLayout = "kik.android.widget.BubbleFramelayout";
    static final String kikRecordTextMgr = "kik.android.util.bx";
    static final String kikConversationFragment = kikFragment + ".KikConversationsFragment";
    static final String kikPKG = "kik.android";
    static final String kABSTRACT_MESSAGE_VIEW_MODEL = "kik.android.chat.vm.messaging.b"/*AbstractMessageViewModel"*/;
    static final String kikReceiptParser = "kik.core.net.h"/*KikXmlParser"*/;
    static final String kikChatFragment = kikFragment + ".KikChatFragment";
    static final String KikApplication = "kik.android.chat.KikApplication";
    static final String KikCoreProvider = "kik.core.y"; //.CoreProvider";
    static final String kikRobotoTextView = kikWidget + ".MessageTextView";
    static final String kikSmileyWidget = kikWidget + ".SmileyWidget";
    static final String kikWebWidget = kikWidget + ".WebWidget";
    static final String kikCamObj = "kik.android.c.d";
    static final String kikStickerWidget = kikWidget + ".StickerWidget";
    static final String kikGifWidget = kikWidget + ".GifWidget";
    static final String kikGalleryWidget = kikWidget + ".GalleryWidget";
    static final String kikAnonymousInterestFilterFragment =  kikFragment + ".AnonymousInterestFilterFragment";
    static final String kikEmojiStatusPickerFragment =  kikFragment + ".EmojiStatusPickerFragment";
    static final String kikAbTestsFragment =  kikFragment + ".AbTestsFragment";
    static final String kikChatBubbleSelectionFragment =  kikFragment + ".ChatBubbleSelectionFragment";
    static final String kikFullScreenAddressbookFragment =  kikFragment + ".FullScreenAddressbookFragment";
    static final String kikInterestsPickerFragment =  kikFragment + ".InterestsPickerFragment";
    static final String kikWelcomeFragment =  kikFragment + ".KikWelcomeFragment";
    static final String kikMakeFriendsOnboardingFragment =  kikFragment + ".MakeFriendsOnboardingFragment";
    static final String kikPaidThemeMarketplaceFragment =  kikFragment + ".PaidThemeMarketplaceFragment";
    static final String kikIqFragmentBase =  kikFragment + ".KikIqFragmentBase";
    static final String kikMultiselectContactsListFragment =  kikFragment + ".KikMultiselectContactsListFragment";
    static final String kikPickUsersFragment =  kikFragment + ".KikPickUsersFragment";
    static final String kikScopedDialogFragment =  kikFragment + ".KikScopedDialogFragment";
    static final String kikAnonMatchingAddFriendDialogFragment =  kikFragment + ".AnonMatchingAddFriendDialogFragment";
    static final String kikContactsListFragment = kikFragment + ".KikContactsListFragment";
    static final String kikBlockedContactsFragment = kikFragment + ".KikBlockedContactsFragment";
}
