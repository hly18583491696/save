package edu.mycc.xhd.mycsdormitorymanagement.controller;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormBuilding;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormRoom;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormAccommodation;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormBuildingService;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormRoomService;
import edu.mycc.xhd.mycsdormitorymanagement.service.DormAccommodationService;
import edu.mycc.xhd.mycsdormitorymanagement.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宿舍管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/dorm")
@CrossOrigin(origins = "*")
public class DormController {
    
    @Autowired
    private DormBuildingService dormBuildingService;
    
    @Autowired
    private DormRoomService dormRoomService;
    
    @Autowired
    private DormAccommodationService dormAccommodationService;
    
    // ========== 楼栋管理 ==========
    
    /**
     * 获取所有楼栋
     */
    @GetMapping("/buildings")
    public Result<List<DormBuilding>> getAllBuildings() {
        log.info("获取所有楼栋列表请求");
        try {
            List<DormBuilding> buildings = dormBuildingService.getAllBuildings();
            log.info("成功获取楼栋列表，共{}条记录", buildings.size());
            return Result.success(buildings);
        } catch (Exception e) {
            log.error("获取楼栋列表失败: {}", e.getMessage());
            return Result.error("获取楼栋列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取楼栋
     */
    @GetMapping("/buildings/{id}")
    public Result<DormBuilding> getBuildingById(@PathVariable Long id) {
        try {
            DormBuilding building = dormBuildingService.getBuildingById(id);
            if (building != null) {
                return Result.success(building);
            } else {
                return Result.error("楼栋不存在");
            }
        } catch (Exception e) {
            return Result.error("获取楼栋信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加楼栋
     */
    @PostMapping("/buildings")
    public Result<String> addBuilding(@RequestBody DormBuilding building) {
        try {
            boolean success = dormBuildingService.addBuilding(building);
            if (success) {
                return Result.success("楼栋添加成功");
            } else {
                return Result.error("楼栋添加失败");
            }
        } catch (Exception e) {
            return Result.error("楼栋添加失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新楼栋
     */
    @PutMapping("/buildings/{id}")
    public Result<String> updateBuilding(@PathVariable Long id, @RequestBody DormBuilding building) {
        try {
            building.setId(id);
            boolean success = dormBuildingService.updateBuilding(building);
            if (success) {
                return Result.success("楼栋更新成功");
            } else {
                return Result.error("楼栋更新失败");
            }
        } catch (Exception e) {
            return Result.error("楼栋更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除楼栋
     */
    @DeleteMapping("/buildings/{id}")
    public Result<String> deleteBuilding(@PathVariable Long id) {
        try {
            boolean success = dormBuildingService.deleteBuilding(id);
            if (success) {
                return Result.success("楼栋删除成功");
            } else {
                return Result.error("楼栋删除失败");
            }
        } catch (Exception e) {
            return Result.error("楼栋删除失败: " + e.getMessage());
        }
    }
    
    // ========== 房间管理 ==========
    
    /**
     * 根据楼栋ID获取房间列表
     */
    @GetMapping("/buildings/{buildingId}/rooms")
    public Result<List<DormRoom>> getRoomsByBuildingId(@PathVariable Long buildingId) {
        try {
            List<DormRoom> rooms = dormRoomService.getRoomsByBuildingId(buildingId);
            return Result.success(rooms);
        } catch (Exception e) {
            return Result.error("获取房间列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据楼栋ID获取可用房间
     */
    @GetMapping("/buildings/{buildingId}/rooms/available")
    public Result<List<DormRoom>> getAvailableRoomsByBuildingId(@PathVariable Long buildingId) {
        try {
            List<DormRoom> rooms = dormRoomService.getAvailableRoomsByBuildingId(buildingId);
            return Result.success(rooms);
        } catch (Exception e) {
            return Result.error("获取可用房间列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取可用房间
     */
    @GetMapping("/rooms/available")
    public Result<List<DormRoom>> getAvailableRooms(@RequestParam(required = false) String gender) {
        try {
            List<DormRoom> rooms;
            if (gender != null && !gender.isEmpty()) {
                rooms = dormRoomService.getAvailableRoomsByGender(gender);
            } else {
                rooms = dormRoomService.getAvailableRooms();
            }
            return Result.success(rooms);
        } catch (Exception e) {
            return Result.error("获取可用房间失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取房间
     */
    @GetMapping("/rooms/{id}")
    public Result<DormRoom> getRoomById(@PathVariable Long id) {
        try {
            DormRoom room = dormRoomService.getRoomById(id);
            if (room != null) {
                return Result.success(room);
            } else {
                return Result.error("房间不存在");
            }
        } catch (Exception e) {
            return Result.error("获取房间信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加房间
     */
    @PostMapping("/rooms")
    public Result<String> addRoom(@RequestBody DormRoom room) {
        try {
            boolean success = dormRoomService.addRoom(room);
            if (success) {
                return Result.success("房间添加成功");
            } else {
                return Result.error("房间添加失败");
            }
        } catch (Exception e) {
            return Result.error("房间添加失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新房间
     */
    @PutMapping("/rooms/{id}")
    public Result<String> updateRoom(@PathVariable Long id, @RequestBody DormRoom room) {
        try {
            room.setId(id);
            boolean success = dormRoomService.updateRoom(room);
            if (success) {
                return Result.success("房间更新成功");
            } else {
                return Result.error("房间更新失败");
            }
        } catch (Exception e) {
            return Result.error("房间更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除房间
     */
    @DeleteMapping("/rooms/{id}")
    public Result<String> deleteRoom(@PathVariable Long id) {
        try {
            boolean success = dormRoomService.deleteRoom(id);
            if (success) {
                return Result.success("房间删除成功");
            } else {
                return Result.error("房间删除失败");
            }
        } catch (Exception e) {
            return Result.error("房间删除失败: " + e.getMessage());
        }
    }
    
    // ==================== 住宿记录管理 ====================
    
    /**
     * 获取所有住宿记录
     */
    @GetMapping("/accommodations")
    public Result<List<DormAccommodation>> getAllAccommodations() {
        try {
            List<DormAccommodation> accommodations = dormAccommodationService.getAllAccommodations();
            return Result.success(accommodations);
        } catch (Exception e) {
            return Result.error("获取住宿记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取单个住宿记录
     */
    @GetMapping("/accommodations/{id}")
    public Result<DormAccommodation> getAccommodationById(@PathVariable Long id) {
        try {
            DormAccommodation accommodation = dormAccommodationService.getAccommodationById(id);
            if (accommodation != null) {
                return Result.success(accommodation);
            } else {
                return Result.error("住宿记录不存在");
            }
        } catch (Exception e) {
            return Result.error("获取住宿记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据学生ID获取住宿记录
     */
    @GetMapping("/accommodations/student/{studentId}")
    public Result<List<DormAccommodation>> getAccommodationsByStudentId(@PathVariable Long studentId) {
        try {
            List<DormAccommodation> accommodations = dormAccommodationService.getAccommodationsByStudentId(studentId);
            return Result.success(accommodations);
        } catch (Exception e) {
            return Result.error("获取学生住宿记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据房间ID获取住宿记录
     */
    @GetMapping("/accommodations/room/{roomId}")
    public Result<List<DormAccommodation>> getAccommodationsByRoomId(@PathVariable Long roomId) {
        try {
            List<DormAccommodation> accommodations = dormAccommodationService.getAccommodationsByRoomId(roomId);
            return Result.success(accommodations);
        } catch (Exception e) {
            return Result.error("获取房间住宿记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 分配宿舍
     */
    @PostMapping("/accommodations")
    public Result<String> assignAccommodation(@RequestBody DormAccommodation accommodation) {
        try {
            boolean success = dormAccommodationService.assignAccommodation(accommodation);
            if (success) {
                return Result.success("分配宿舍成功");
            } else {
                return Result.error("分配宿舍失败");
            }
        } catch (Exception e) {
            return Result.error("分配宿舍失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新住宿记录
     */
    @PutMapping("/accommodations/{id}")
    public Result<String> updateAccommodation(@PathVariable Long id, @RequestBody DormAccommodation accommodation) {
        try {
            accommodation.setId(id);
            boolean success = dormAccommodationService.updateAccommodation(accommodation);
            if (success) {
                return Result.success("更新住宿记录成功");
            } else {
                return Result.error("更新住宿记录失败");
            }
        } catch (Exception e) {
            return Result.error("更新住宿记录失败: " + e.getMessage());
        }
    }

    /**
     * 学生退宿
     */
    @PutMapping("/accommodations/checkout/student/{studentId}")
    public Result<String> checkOutByStudentId(@PathVariable Long studentId) {
        try {
            boolean success = dormAccommodationService.checkOutByStudentId(studentId);
            if (success) {
                return Result.success("退宿成功");
            } else {
                return Result.error("退宿失败");
            }
        } catch (Exception e) {
            return Result.error("退宿失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除住宿记录
     */
    @DeleteMapping("/accommodations/{id}")
    public Result<String> deleteAccommodation(@PathVariable Long id) {
        log.info("删除住宿记录请求，ID: {}", id);
        try {
            boolean success = dormAccommodationService.deleteAccommodation(id);
            if (success) {
                log.info("成功删除住宿记录，ID: {}", id);
                return Result.success("删除住宿记录成功");
            } else {
                log.warn("删除住宿记录失败，ID: {}", id);
                return Result.error("删除住宿记录失败");
            }
        } catch (Exception e) {
            log.error("删除住宿记录异常，ID: {}, 错误: {}", id, e.getMessage());
            return Result.error("删除住宿记录失败: " + e.getMessage());
        }
    }
}