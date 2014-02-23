/**
 * Copyright (c) 2011-2013, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.core;

import com.jfinal.render.ViewType;
import com.jfinal.sog.kit.StringPool;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;

/**
 * Global constants definition
 */
public interface Const {

    String JFINAL_VERSION = "1.5";

    ViewType DEFAULT_VIEW_TYPE = ViewType.FREE_MARKER;

    String DEFAULT_ENCODING = "utf-8";

    String DEFAULT_URL_PARA_SEPARATOR = StringPool.DASH;

    String DEFAULT_FILE_CONTENT_TYPE = "application/octet-stream";

    String DEFAULT_JSP_EXTENSION = ".jsp";

    // sog 修改默认的freemarker后缀
    String DEFAULT_FREE_MARKER_EXTENSION = ".ftl";            // The original is ".ftl", Recommend ".html"

    String DEFAULT_VELOCITY_EXTENSION = ".vm";

    // "WEB-INF/download" + File.separator maybe better otherwise it can be downloaded by browser directly
    String DEFAULT_FILE_RENDER_BASE_PATH = File.separator + "download" + File.separator;

    int DEFAULT_MAX_POST_SIZE = 1024 * 1024 * 10;            // Default max post size of multipart request: 10 Meg

    String I18N_LOCALE = "__I18N_LOCALE__";                    // The i18n name of cookie

    int DEFAULT_I18N_MAX_AGE_OF_COOKIE = 999999999;

    int DEFAULT_FREEMARKER_TEMPLATE_UPDATE_DELAY = 3600;    // For not devMode only

    String DEFAULT_TOKEN_NAME = "jfinal_token";

    int DEFAULT_SECONDS_OF_TOKEN_TIME_OUT = 900;            // 900 seconds ---> 15 minutes

    int MIN_SECONDS_OF_TOKEN_TIME_OUT = 300;                // 300 seconds ---> 5 minutes


    String DEFAULT_FORMAT = "YYYY-MM-DD hh:mm:ss.mss";

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DEFAULT_FORMAT);
}







