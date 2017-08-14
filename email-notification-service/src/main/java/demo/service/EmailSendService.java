package demo.service;

import demo.domain.User;
import demo.model.DealInfo;

import java.util.List;

/**
 * Created by jiaxu on 8/14/17.
 */
public interface EmailSendService {

    void sendToSubscribers(int categoryId, List<DealInfo> dealInfoList);

}
