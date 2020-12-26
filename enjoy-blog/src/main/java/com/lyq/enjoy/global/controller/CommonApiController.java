package com.lyq.enjoy.global.controller;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.common.plugin.CodeCache;
import com.lyq.framework.common.response.R;
import com.lyq.framework.util.DataStore;
import com.lyq.framework.util.FastDFSUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/commonapi")
public class CommonApiController {

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public R getCodeByDmbh(@RequestParam(name = "dmbh") String dmbhPara) throws AppException {
        HashMap<String, DataStore> returnValue = new HashMap<String, DataStore>();
        String[] dmbhArr = dmbhPara != null ? dmbhPara.split(",") : null;

        if (dmbhArr != null) {
            for (int i = 0; i < dmbhArr.length; i++) {
                Map<String, String> codeMap = CodeCache.getCodeMap(dmbhArr[i]);

                DataStore codeInfo = DataStore.getInstance();
                if (codeMap != null) {
                    for (Map.Entry<String, String> each : codeMap.entrySet()) {
                        codeInfo.addRow();
                        codeInfo.put(codeInfo.rowCount() - 1, "code", each.getKey());
                        codeInfo.put(codeInfo.rowCount() - 1, "content", each.getValue());
                    }
                }

                returnValue.put(dmbhArr[i], codeInfo);
            }
        }

        return R.ok().data(returnValue);
    }

    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public String uploadfile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = FastDFSUtil.upload(file);
        System.out.println(path);
        return path;
    }
}
