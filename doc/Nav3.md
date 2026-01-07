Impossible to make this class serializable because its parent is not serializable and does not have exactly one constructor without parameters
V3ExploreRecommendBean


Redeclaration:
data class WechatPaymentSignBean : Any
class WechatPaymentSignBean : Any
WechatPaymentSignBean


WorkSharePlatformConfig

AiCoverMaterialsBean


AIMVLyricSegmentResult
result = 31 * result + Boolean.hashCode(isSelectAllInternal)
Too many arguments for 'fun hashCode(): Int'.


AiRewordLyricTagsChooseExtData


Redeclaration:
data class AlbumLyricsBean : Any
class AlbumLyricsBean : Any
AlbumLyricsBean


Redeclaration:
data class AuditTrackTipBean : Any
class AuditTrackTipBean : Any
AuditTrackTipBean


Class 'CoverWordSongTagsBean' is not abstract and does not implement abstract base class member:
fun getImageUrl(): String!
CoverWordSongTagsBean

Redeclaration:
data class CreditDetailBean : Any
class CreditDetailBean : Any
CreditDetailBean


Redeclaration:
data class ImagePreviewBean : Parcelable
class ImagePreviewBean : Any, Parcelable

ImagePreviewBean

LyricsItemBean

Conflicting overloads:
constructor(startTime: Double, endTime: Double): LyricsTimeRangeBean
Conflicting overloads:
constructor(startTime: Double, endTime: Double): LyricsTimeRangeBean
LyricsTimeRangeBean

MemberEquityComparisonTableAuthNameColumn

MemberEquityComparisonTableGoodsGroupColumn

MV2Storyboard
Too many arguments for 'fun hashCode(): Int'.

ktlion data class 本来有自动生成的hashCode方法，但是当我们自己定义了hashCode方法之后，自动生成的就会失效，所以会报错说参数太多。

如果遇到Parcelable缺少的，需要加上@Parcelize 注解，并且确保类实现了Parcelable接口。


MVCharacterUploadInfo