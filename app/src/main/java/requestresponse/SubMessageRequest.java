package requestresponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by danu on 6/1/17.
 */

public class SubMessageRequest {
    @SerializedName("publish")
    private List<SubMessageRequestBean> subMessageRequestBeanList;

    public List<SubMessageRequestBean> getSubMessageRequestBeanList() {
        return subMessageRequestBeanList;
    }

    public void setSubMessageRequestBeanList(List<SubMessageRequestBean> subMessageRequestBeanList) {
        this.subMessageRequestBeanList = subMessageRequestBeanList;
    }
}
