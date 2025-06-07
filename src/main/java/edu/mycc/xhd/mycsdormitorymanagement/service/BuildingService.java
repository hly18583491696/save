package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.Building;
import edu.mycc.xhd.mycsdormitorymanagement.common.PageRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 宿舍楼服务接口
 * 
 * @author 开发团队
 * @since 2024-01-01
 */
public interface BuildingService {

    /**
     * 根据ID查询宿舍楼
     * 
     * @param id 宿舍楼ID
     * @return 宿舍楼信息
     */
    Building getById(Long id);

    /**
     * 根据楼栋编号查询宿舍楼
     * 
     * @param buildingNumber 楼栋编号
     * @return 宿舍楼信息
     */
    Building getByBuildingNumber(String buildingNumber);

    /**
     * 根据楼栋类型查询宿舍楼列表
     * 
     * @param buildingType 楼栋类型
     * @return 宿舍楼列表
     */
    List<Building> getByBuildingType(String buildingType);

    /**
     * 根据状态查询宿舍楼列表
     * 
     * @param status 状态
     * @return 宿舍楼列表
     */
    List<Building> getByStatus(String status);

    /**
     * 分页查询宿舍楼
     * 
     * @param pageRequest 分页请求参数
     * @return 分页结果
     */
    IPage<Building> getPage(PageRequest pageRequest);

    /**
     * 获取所有宿舍楼列表
     * 
     * @return 宿舍楼列表
     */
    List<Building> getAll();

    /**
     * 创建宿舍楼
     * 
     * @param building 宿舍楼信息
     * @return 创建的宿舍楼
     */
    Building create(Building building);

    /**
     * 更新宿舍楼信息
     * 
     * @param id 宿舍楼ID
     * @param building 宿舍楼信息
     * @return 更新后的宿舍楼
     */
    Building update(Long id, Building building);

    /**
     * 删除宿舍楼
     * 
     * @param id 宿舍楼ID
     * @return 是否删除成功
     */
    boolean delete(Long id);

    /**
     * 批量删除宿舍楼
     * 
     * @param ids 宿舍楼ID列表
     * @return 是否删除成功
     */
    boolean deleteBatch(List<Long> ids);

    /**
     * 启用宿舍楼
     * 
     * @param id 宿舍楼ID
     * @return 是否启用成功
     */
    boolean enable(Long id);

    /**
     * 禁用宿舍楼
     * 
     * @param id 宿舍楼ID
     * @return 是否禁用成功
     */
    boolean disable(Long id);

    /**
     * 检查楼栋编号是否存在
     * 
     * @param buildingNumber 楼栋编号
     * @return 是否存在
     */
    boolean existsByBuildingNumber(String buildingNumber);

    /**
     * 检查楼栋编号是否存在（排除指定ID）
     * 
     * @param buildingNumber 楼栋编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByBuildingNumber(String buildingNumber, Long excludeId);

    /**
     * 获取宿舍楼统计信息
     * 
     * @return 统计信息
     */
    Object getStatistics();

}