package com.tony.sharpdownload;

/**
 * @author Tony
 * @version 1.0
 *          <p><strong>Features draft description.主要功能介绍<></p>
 * @since 2017/4/28 13:54
 */
public interface SharpDownloadStatus {
    int WAIT = 0x00000001;
    int START = 0x00000002;
    int PAUSE = 0x00000003;
    int DOWNLOADING = 0x00000003;
    int FINISH = 0x00000004;
    int ERROR = 0x00000005;
    int CANCEL = 0x00000005;
}
