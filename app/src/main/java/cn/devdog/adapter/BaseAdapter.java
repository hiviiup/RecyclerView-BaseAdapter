package cn.devdog.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter 基类
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public Context mContext;

    //数据集合
    private List<T> datas;

    //header view
    private View mHeaderView = null;

    //load more listener
    private OnLoadMoreListener mLoadMoreListener = null;

    //item click listener
    private OnItemClickListener mItemClickListener;

    private OnItemLongClickListener mItemLongClickListener;

    //item view type : load more
    private static final int VIEW_TYPE_LOAD_MORE = -1;

    //item view type : header view
    private static final int VIEW_TYPE_HEADER = -2;

    //load more view, you can set text by this view
    public TextView textView;


    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener
    {
        void onItemLongClick(int position);
    }

    public interface OnLoadMoreListener
    {
        void onLoadMore();

        RecyclerView getRecyclerView();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        if (mItemClickListener == null) this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener)
    {
        if (mItemLongClickListener == null) this.mItemLongClickListener = listener;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener listener)
    {
        if (mLoadMoreListener == null) this.mLoadMoreListener = listener;
        configLoadListener(mLoadMoreListener.getRecyclerView());
    }

    public BaseAdapter(Context mContext)
    {
        this.mContext = mContext;
        this.datas = new ArrayList<>();
    }

    /**
     * add object into list
     */
    public void addObject(T t)
    {
        datas.add(t);
    }

    /**
     * reset list
     */
    public void addAll(List<T> list)
    {
        datas.clear();
        datas.addAll(list);
    }

    /**
     * remove item
     */
    public void remove(int position)
    {
        datas.remove(position);
    }

    /**
     * get item by position
     */
    public T getObject(int position)
    {
        return datas.get(position);
    }

    /**
     * get recyclerview item count
     */
    @Override
    public int getItemCount()
    {
        int i = mLoadMoreListener != null ? datas.size() + 1 : datas.size();
        int i1 = getHeaderView() == null ? i : i + 1;
        return i1;
    }

    /**
     * get recyclerview item count without headview and load more view;
     */
    public int getContentCount()
    {
        return datas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder holder;

        //need load more but don't have header view
        if (mLoadMoreListener != null && getHeaderView() == null)
        {
            if (viewType == VIEW_TYPE_LOAD_MORE)
                holder = new LoadMoreViewHolder(initLoadMoreView());
            else holder = createMyViewHolder(parent, viewType);
        }
        //need load more and header view
        else if (mLoadMoreListener != null && getHeaderView() != null)
        {
            if (viewType == VIEW_TYPE_LOAD_MORE)
                holder = new LoadMoreViewHolder(initLoadMoreView());
            else if (viewType == VIEW_TYPE_HEADER) holder = new HeaderViewHolder(getHeaderView());
            else holder = createMyViewHolder(parent, viewType);
        }
        //need header but don't have load more
        else if (mLoadMoreListener == null && getHeaderView() != null)
        {
            if (viewType == VIEW_TYPE_HEADER) holder = new HeaderViewHolder(getHeaderView());
            else holder = createMyViewHolder(parent, viewType);
        }
        //only content
        else
        {
            holder = createMyViewHolder(parent, viewType);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder k, int position)
    {

        if (getItemViewType(position) != VIEW_TYPE_HEADER && getItemViewType(position) != VIEW_TYPE_LOAD_MORE)
        {
            bindDataAndAddClickListener(k, getItemViewType(0) == VIEW_TYPE_HEADER ? position - 1 : position,
                    getItemViewType(position));
        }

    }


    @Override
    public int getItemViewType(int position)
    {
        if (mLoadMoreListener != null && getHeaderView() == null)
        {
            return position == (getItemCount() - 1) ?
                    VIEW_TYPE_LOAD_MORE : getViewType(position);
        } else if (mLoadMoreListener != null && getHeaderView() != null)
        {
            if (position == 0)
                return VIEW_TYPE_HEADER;
            else if (position == getItemCount() - 1)
                return VIEW_TYPE_LOAD_MORE;
            else
                return getViewType(position - 1);
        } else if (mLoadMoreListener == null && getHeaderView() != null)
        {
            return position == 0 ?
                    VIEW_TYPE_HEADER : getViewType(position-1);
        } else
        {
            return getViewType(position);
        }
    }

    /**
     * 获取view type
     */
    protected abstract int getViewType(int position);

    /**
     * create view Holder
     */
    protected abstract RecyclerView.ViewHolder createMyViewHolder(ViewGroup parent, int viewType);

    private void bindDataAndAddClickListener(RecyclerView.ViewHolder holder, final int position, int viewType)
    {
        if (mItemClickListener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mItemClickListener.onItemClick(position);
                }
            });

        if (mItemLongClickListener != null)
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mItemLongClickListener.onItemLongClick(position);
                    return true;
                }
            });

        bindData(holder, position, viewType);
    }

    /**
     * bind data
     */
    protected abstract void bindData(RecyclerView.ViewHolder holder, int position, int viewType);

    private TextView initLoadMoreView()
    {
        textView = new TextView(mContext);

        textView.setText("加载更多");
        textView.setTextColor(0xff666666);
        textView.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 30;
        params.bottomMargin = 30;
        textView.setLayoutParams(params);

        return textView;
    }

    /**
     * add header view
     */
    public void addHeaderView(View view)
    {
        mHeaderView = view;
    }

    /**
     * 获取headerview
     *
     * @return
     */
    private View getHeaderView()
    {
        return mHeaderView;
    }


    /**
     * 当需要加载更多的功能的时候，给recycleview添加滑动监听事件
     * <p/>
     * 此处有一个bug，就是当设置的加载数量不能填充整个屏幕时，就无法执行加载更多的操作了，所以还需要设置当未满屏幕，最后一条可见时，也应该执行加载更多
     */
    private void configLoadListener(RecyclerView mRecyclerView)
    {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            //是否是向下滑动的
            boolean isSlidingBottom = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                //获取LinearLayoutManager
                LinearLayoutManager mManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    //获取最后一个item的position
                    final int lastVisibleItemPosition = mManager.findLastVisibleItemPosition();
                    final int itemCount = mManager.getItemCount();
                    if (lastVisibleItemPosition == itemCount - 1) //说明滑动到了底部
                    {
                        //Toast.makeText(mContext,"滑动到底部了",Toast.LENGTH_SHORT).show();
                        mLoadMoreListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                //判断是否是向下活动
                isSlidingBottom = dy > 0 ? true : false;
            }
        });
    }


    /**
     * 加载更多的holder
     */
    private class LoadMoreViewHolder extends RecyclerView.ViewHolder
    {
        public LoadMoreViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    /**
     * 头布局的viewholder
     */
    private class HeaderViewHolder extends RecyclerView.ViewHolder
    {

        public HeaderViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
