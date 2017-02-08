package cn.devdog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.devdog.adapter.BaseAdapter;
import cn.devdog.adapter.R;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //demo
        //设置点击事件
        //new BaseAdapter<>().setOnItemClickListener();

        //设置加载更多监听
        //new BaseAdapter<>().setOnLoadMoreListener();

        //设置长时间点击事件
        //new BaseAdapter<>().setOnItemLongClickListener();

        //添加头布局
        //new BaseAdapter<>().addHeaderView();

        //获取当前条目对象
        //new BaseAdapter<>().getObject(position);

        //删除某一条
        //new BaseAdapter<>().remove(position);

        //增加
        //new BaseAdapter<>().addObject(object);

        //重新增加全部数据
        //new BaseAdapter<>().addAll(list);

        //加载更多控件文本设置
        //new BaseAdapter<>().textView.setText("");

    }
}
