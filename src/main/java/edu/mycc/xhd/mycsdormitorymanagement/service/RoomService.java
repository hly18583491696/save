package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import edu.mycc.xhd.mycsdormitorymanagement.common.PageRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 宿舍房间服务接口
 * 
 * @author 开发团队
 * @since 
 */
public interface RoomService {

    /**
     * 根据ID查询房间
     * 
     * @param id 房间ID
     * @return 房间信息
     */
    DormRoom getById(Long id);

    /**
     * 根据房间号查询房间
     * 
     * @param roomNumber 房间号
     * @return 房间信息
     */
    DormRoom getByRoomNumber(String roomNumber);

    /**
     * 根据楼栋ID查询房间列表
     * 
     * @param buildingId 楼栋ID
     * @return 房间列表
     */
    List<DormRoom> getByBuildingId(Long buildingId);

    /**
     * 根据房间状态查询房间列表
     * 
     * @param roomStatus 房间状态
     * @return 房间列表
     */
    List<DormRoom> getByRoomStatus(String roomStatus);

    /**
     * 查询可用房间列表
     * 
     * @return 可用房间列表
     */
    List<DormRoom> getAvailableRooms();

    /**
     * 根据楼栋ID查询可用房间列表
     * 
     * @param buildingId 楼栋ID
     * @return 可用房间列表
     */
    List<DormRoom> getAvailableRoomsByBuildingId(Long buildingId);

    /**
     * 分页查询房间
     * 
     * @param pageRequest 分页请求参数
     * @return 分页结果
     */
    IPage<DormRoom> getPage(PageRequest pageRequest);

    /**
     * 获取所有房间列表
     * 
     * @return 房间列表
     */
    List<DormRoom> getAll();

    /**
     * 创建房间
     * 
     * @param room 房间信息
     * @return 创建的房间
     */
    DormRoom create(DormRoom room);

    /**
     * 更新房间信息
     * 
     * @param id 房间ID
     * @param room 房间信息
     * @return 更新后的房间
     */
    DormRoom update(Long id, DormRoom room);

    /**
     * 删除房间
     * 
     * @param id 房间ID
     * @return 是否删除成功
     */
    boolean delete(Long id);

    /**
     * 批量删除房间
     * 
     * @param ids 房间ID列表
     * @return 是否删除成功
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 更新房间床位数
     * 
     * @param roomId 房间ID
     * @param occupiedBeds 已入住床位数
     * @return 是否更新成功
     */
    boolean updateOccupiedBeds(Long roomId, Integer occupiedBeds);

    /**
     * 增加房间已入住床位数
     * 
     * @param roomId 房间ID
     * @param count 增加数量
     * @return 是否更新成功
     */
    boolean increaseOccupiedBeds(Long roomId, Integer count);

    /**
     * 减少房间已入住床位数
     * 
     * @param roomId 房间ID
     * @param count 减少数量
     * @return 是否更新成功
     */
    boolean decreaseOccupiedBeds(Long roomId, Integer count);

    /**
     * 检查房间号是否存在
     * 
     * @param roomNumber 房间号
     * @return 是否存在
     */
    boolean existsByRoomNumber(String roomNumber);

    /**
     * 检查房间号是否存在（排除指定ID）
     * 
     * @param roomNumber 房间号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByRoomNumber(String roomNumber, Long excludeId);

    /**
     * 检查房间是否有空余床位
     * 
     * @param roomId 房间ID
     * @return 是否有空余床位
     */
    boolean hasAvailableBeds(Long roomId);

    /**
     * 获取房间统计信息
     * 
     * @return 统计信息
     */
    Object getStatistics();

    /**
     * 根据楼栋ID获取房间统计信息
     * 
     * @param buildingId 楼栋ID
     * @return 统计信息
     */
    Object getStatisticsByBuildingId(Long buildingId);

}