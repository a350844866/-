package com.jt.order.job;

import com.jt.order.mapper.OrderMapper;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

//需要继承定时任务的接口,之后回显方法
public class PaymentOrderJob extends QuartzJobBean {

//    @Autowired  由于容器不一样,所以spring容器不能直接注入
//    private OrderMapper orderMapper;

    //需要继承定时任务的接口,之后回显方法
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //如果触发器监控到时间已经到了,则开始会回调该方法
        /**
         *  1.从定时任务中获取spring容器
         *  2.获取orderMapper对象
         *  3.通过mapper对象执行任务操作
         */
        ApplicationContext applicationContext =
                (ApplicationContext) context.getJobDetail().getJobDataMap().get("applicationCOntext");
        OrderMapper orderMapper = applicationContext.getBean(OrderMapper.class);

        //定义2天前的时间
        Date twoDay = new DateTime().minusDays(2).toDate();
        orderMapper.updateStatus();
        System.out.println("定时任务执行成功");
    }
}
