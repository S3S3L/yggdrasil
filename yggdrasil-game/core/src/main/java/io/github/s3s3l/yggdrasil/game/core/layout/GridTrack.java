package io.github.s3s3l.yggdrasil.game.core.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 网格轨道类
 * 表示网格的行或列模板，包含多个轨道尺寸
 */
public class GridTrack {
    /** 轨道列表 */
    private final List<LengthUnit> tracks;

    /**
     * 默认构造函数，创建空轨道列表
     */
    public GridTrack() {
        this.tracks = new ArrayList<>();
    }

    /**
     * 构造函数，使用可变参数初始化轨道
     * @param tracks 轨道尺寸数组
     */
    public GridTrack(LengthUnit... tracks) {
        this.tracks = new ArrayList<>(Arrays.asList(tracks));
    }

    /**
     * 添加一个轨道
     * @param track 轨道尺寸
     */
    public void addTrack(LengthUnit track) {
        this.tracks.add(track);
    }

    /**
     * 获取所有轨道
     * @return 轨道列表
     */
    public List<LengthUnit> getTracks() {
        return tracks;
    }

    /**
     * 获取轨道数量
     * @return 轨道数量
     */
    public int getTrackCount() {
        return tracks.size();
    }

    /**
     * 获取指定索引的轨道
     * @param index 索引
     * @return 轨道尺寸
     */
    public LengthUnit getTrack(int index) {
        return tracks.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tracks.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(tracks.get(i).toString());
        }
        return sb.toString();
    }
}
