package demo;

import demo.model.DealInfo;
import demo.service.EmailSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jiaxu on 8/13/17.
 */
@Component
@Slf4j
public class DealInfoReceiver {

    private static final int TOP_K = 2;

    @Autowired
    private EmailSendService emailSendService;

    Comparator<DealInfo> comparator = new Comparator<DealInfo>() {
        @Override
        public int compare(DealInfo o1, DealInfo o2) {
            if(o1.getDiscountPercent() == o2.getDiscountPercent()) {
                return 0;
            } else if(o1.getDiscountPercent() > o2.getDiscountPercent()) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    Map<Integer, PriorityQueue<DealInfo>> priorityQueueMap = new HashMap<>();
    private static final int[] CATEGORY_IDS = {5, 9, 10, 25};

    public DealInfoReceiver() {
        initPriorityQueueMap();
    }

    private void initPriorityQueueMap() {
        for(int id : CATEGORY_IDS) {
            priorityQueueMap.put(id, new PriorityQueue<>(TOP_K, comparator));
        }
    }


    @RabbitListener(queues = "priceMonitoring.deals.all")
    public void processDeals(DealInfo dealInfo) {
        int categoryId = dealInfo.getCategoryId();
        addDealInfoToPQ(priorityQueueMap.get(categoryId), dealInfo);
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    void distributeDeals() {
        for(int id : CATEGORY_IDS) {
            PriorityQueue<DealInfo> priorityQueue = priorityQueueMap.get(id);
            if(priorityQueue.size() < TOP_K) continue;
            List<DealInfo> dealInfoList = prepareDeals(priorityQueue);
            this.emailSendService.sendToSubscribers(id, dealInfoList);
        }
    }

    synchronized List<DealInfo> prepareDeals(PriorityQueue<DealInfo> pq) {
        List<DealInfo> res = new ArrayList<>();
        while(pq.size() > 0) {
            res.add(pq.poll());
        }
        return res;
    }


    private void addDealInfoToPQ(PriorityQueue<DealInfo> pq, DealInfo dealInfo) {
        if(pq.size() == TOP_K) {
            pq.poll();
        }
        pq.add(dealInfo);
    }


}
