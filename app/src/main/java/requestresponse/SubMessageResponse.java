package requestresponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by danu on 6/1/17.
 */

public class SubMessageResponse {
    @SerializedName("results")
    private List<SubMessageResponseBean> subMessageResponseBeanList;

    public List<SubMessageResponseBean> getSubMessageResponseBeanList() {
        return subMessageResponseBeanList;
    }

    public void setSubMessageResponseBeanList(List<SubMessageResponseBean> subMessageResponseBeanList) {
        this.subMessageResponseBeanList = subMessageResponseBeanList;
    }
}
