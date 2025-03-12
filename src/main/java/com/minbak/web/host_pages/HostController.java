package com.minbak.web.host_pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minbak.web.file_upload.FileService;
import com.minbak.web.file_upload.ImageFileDto;
import com.minbak.web.host_pages.dto.CreateImageDto;
import com.minbak.web.spring_security.CustomUserDetails;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import com.minbak.web.host_pages.dto.HostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/host")
@SessionAttributes("hostDto")
public class HostController {
    @Autowired
    private HostService hostService;
    @Autowired
    private GetUserNameService getUserNameService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CreateHostMapper createHostMapper;

    @ModelAttribute("hostDto")
    public HostDto hostDto() {
        return new HostDto(); // 세션에 없으면 새로운 객체 반환
    }

    @GetMapping("/create-rooms")
    public String createRooms(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @ModelAttribute("hostDto") HostDto hostDto,
                              Model model) {
        // userId가 설정되지 않았다면 로그인된 사용자의 ID(1번) 설정
        if(userDetails != null){
            hostDto.setUserId(userDetails.getUserId());
            System.out.println("로그인한 userId: " + userDetails.getUserId());
        }

        // userName이 아직 설정되지 않았다면 DB에서 가져오기
        if (hostDto.getUserName() == null) {
            String userName = getUserNameService.getUserName(hostDto.getUserId());
            hostDto.setUserName(userName);
            System.out.println("DB에서 가져온 userName: " + userName); // 로그 확인
        }

        model.addAttribute("name", hostDto.getUserName());
        return "host-pages/rooms_create";
    }

    @GetMapping("/overview")
    public String overview(){
        return "host-pages/overview";
    }

    @GetMapping("/place")
    public String place(){
        return "host-pages/place";
    }
    // 숙소 유형 선택 페이지
    @GetMapping("/type")
    public String roomsType(@ModelAttribute("hostDto") HostDto hostDto,Model model){
        model.addAttribute("selectedType",hostDto.getBuildingType());
        return "host-pages/type";
    }
    // 숙소 유형 저장 후 다음 페이지 이동
    @PostMapping("/type/save")
    @ResponseBody // AJAX 응답
    public String saveBuildingType(@ModelAttribute("hostDto") HostDto hostDto, @RequestParam("buildingType") String buildingType) {
        hostDto.setBuildingType(buildingType);
        System.out.println("🏡 선택한 숙소 유형 저장: " + hostDto.getBuildingType()); // ✅ 콘솔 로그 확인
        return "success"; // AJAX 요청 완료 응답
    }


    @GetMapping("/location")
    public String roomLocation(@ModelAttribute("hostDto") HostDto hostDto,Model model){
        model.addAttribute("selectedAddress", hostDto.getAddress());
        model.addAttribute("latitude", hostDto.getLatitude());
        model.addAttribute("longitude", hostDto.getLongitude());
        return "host-pages/location";
    }
    @PostMapping("/location/save")
    public String saveLocation(@ModelAttribute("hostDto") HostDto hostDto,
                               @RequestParam("province") String province,
                               @RequestParam("city") String city,
                               @RequestParam("district") String district,
                               @RequestParam("roadAddress") String roadAddress,
                               @RequestParam(value = "buildingName", required = false) String buildingName,
                               @RequestParam(value = "postalCode", required = false) String postalCode,
                               @RequestParam("latitude") Double latitude,
                               @RequestParam("longitude") Double longitude) {

        // 🔹 주소를 하나의 문자열로 조합하여 저장
        String fullAddress = String.format("%s %s %s %s %s %s",
                province, city, district, roadAddress,
                (buildingName != null && !buildingName.isEmpty()) ? buildingName : "",
                (postalCode != null && !postalCode.isEmpty()) ? "(우편번호: " + postalCode + ")" : "");

        // 🔹 `HostDto`에 값 저장
        hostDto.setAddress(fullAddress.trim()); // 불필요한 공백 제거 후 저장
        hostDto.setLatitude(latitude);
        hostDto.setLongitude(longitude);

        // ✅ 콘솔 로그로 값 확인
        System.out.println("📌 [저장할 주소 데이터]");
        System.out.println("도/특별-광역시: " + province);
        System.out.println("도시: " + city);
        System.out.println("군/구: " + district);
        System.out.println("도로명 주소: " + roadAddress);
        System.out.println("건물명: " + buildingName);
        System.out.println("우편번호: " + postalCode);
        System.out.println("최종 저장할 주소: " + fullAddress);
        System.out.println("위도: " + latitude);
        System.out.println("경도: " + longitude);

        return "redirect:/host/floor-plan"; // 다음 페이지로 이동
    }

    @GetMapping("/floor-plan")
    public String floorPlan(){
        return "host-pages/floor-plan";
    }
    @PostMapping("/floor-plan/save")
    public String saveFloorPlan(@ModelAttribute("hostDto") HostDto hostDto,
                                @RequestParam("maxGuests") Integer maxGuests,
                                @RequestParam("bedrooms") Integer bedrooms,
                                @RequestParam("beds") Integer beds,
                                @RequestParam("bathrooms") Integer bathrooms){

        hostDto.setMaxGuests(maxGuests);
        hostDto.setBedrooms(bedrooms);
        hostDto.setBeds(beds);
        hostDto.setBathrooms(bathrooms);
        // ✅ 콘솔 로그 확인
        System.out.println("📌 [저장된 숙소 기본 정보]");
        System.out.println("게스트 수: " + hostDto.getMaxGuests());
        System.out.println("침실 수: " + hostDto.getBedrooms());
        System.out.println("침대 수: " + hostDto.getBeds());
        System.out.println("욕실 수: " + hostDto.getBathrooms());

        return "redirect:/host/charm"; // ✅ 다음 페이지로 이동
    }

    @GetMapping("/charm")
    public String charm(){
        return "host-pages/accommodation-charm";
    }

    @GetMapping("/option")
    public String roomsOption(){
        return "host-pages/option";
    }
    // ✅ 옵션 저장 (사용자가 선택한 옵션을 세션에 저장)
    @PostMapping("/option/save")
    public String saveOptions(@ModelAttribute("hostDto") HostDto hostDto,
                              @RequestParam("optionIds") String optionIdsString) {
        try {
            // ✅ 쉼표(,)로 구분된 문자열을 `List<Integer>`로 변환
            List<Integer> selectedOptions = Arrays.stream(optionIdsString.split(","))
                    .map(String::trim)  // 공백 제거
                    .map(Integer::parseInt) // Integer 변환
                    .collect(Collectors.toList());

            hostDto.setOptionIds(selectedOptions);
            System.out.println("📌 [저장된 숙소 옵션] " + selectedOptions);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 옵션 변환 오류 발생!");
        }

        return "redirect:/host/category"; // 다음 단계로 이동
    }



    @GetMapping("/category")
    public String roomsCategories() {
        return "host-pages/category";
    }

    // ✅ 선택한 카테고리 저장 (사용자가 선택한 카테고리를 세션에 저장)
    @PostMapping("/category/save")
    public String saveCategories(@ModelAttribute("hostDto") HostDto hostDto,
                                 @RequestParam("categoryIds") String categoryIdsString) {
        try {
            // ✅ 쉼표(,)로 구분된 문자열을 `List<Integer>`로 변환
            List<Integer> selectedCategories = Arrays.stream(categoryIdsString.split(","))
                    .map(String::trim)  // 공백 제거
                    .map(Integer::parseInt) // Integer 변환
                    .collect(Collectors.toList());

            hostDto.setCategoryIds(selectedCategories);
            System.out.println("📌 [저장된 숙소 카테고리] " + selectedCategories);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 카테고리 변환 오류 발생!");
        }

        return "redirect:/host/photos"; // 다음 단계로 이동
    }






    @GetMapping("/photos")
    public String photos(){
        return "host-pages/photos";
    }
    @Value("${file.upload.directory}")
    private String uploadDirectory;
    // ✅ 사진 저장 (사용자가 업로드한 이미지 리스트를 세션에 저장)
    @PostMapping("/photos/save")
    public String savePhotos(@ModelAttribute("hostDto") HostDto hostDto,
                             @RequestParam("files") MultipartFile[] files) {

        List<String> fileUrls = new ArrayList<>();
        for(MultipartFile file : hostDto.getFiles()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;


                //실제로 파일을 저장할 위치
                Path filePath = Paths.get(uploadDirectory, uniqueFilename);
                Files.copy(file.getInputStream(), filePath);

                ImageFileDto imageFile = hostService.saveFile(uniqueFilename, originalFilename ,(int) file.getSize(),0 ,"rooms");
                fileUrls.add(uniqueFilename);

            } catch (IOException e) {
                System.out.println(e);
                return "redirect:/host/photos";
            }
        }

        hostDto.setFileUrls(fileUrls);

        System.out.println(Arrays.toString(files));


        return "redirect:/host/roomName";
    }





    @GetMapping("/roomName")
    public String roomsName(){
        return "host-pages/roomName";
    }
    @PostMapping("roomName/save")
    public String saveRoomName(@ModelAttribute("hostDto") HostDto hostDto,
                               @RequestParam("name") String name){
        hostDto.setName(name);
        System.out.println("📌 [저장된 숙소 이름]: " + name);
        return "redirect:/host/title"; // ✅ 다음 페이지 이동
    }


    @GetMapping("/title")
    public String roomsTitle(){
        return "host-pages/title";
    }
    @PostMapping("/title/save")
    public String saveTitle(@ModelAttribute("hostDto") HostDto hostDto,
                            @RequestParam("title") String title){
        if(title == null || title.trim().isEmpty()){
            return "redirect:/host/title?error";
        }

        hostDto.setTitle(title);
        System.out.println("📌 [숙소 제목 저장 완료]: " + hostDto.getTitle());

        return "redirect:/host/description";

    }

    @GetMapping("/description")
    public String description(){
        return "host-pages/description";
    }
    @PostMapping("/description/save")
    public String saveDescription(@ModelAttribute("hostDto") HostDto hostDto,
                                  @RequestParam("content") String content){
        if(content == null || content.trim().isEmpty()){
            return "redirect:/host/description?error"; // 빈 값이면 다시 입력
        }

        hostDto.setContent(content);
        System.out.println("📌 [숙소 설명 저장 완료]: " + hostDto.getContent());

        return "redirect:/host/useGuide";
    }

    @GetMapping("useGuide")
    public String roomsUseGuide(){
        return "host-pages/useGuide";
    }
    @PostMapping("/useGuide/save")
    public String saveUseGuide(@ModelAttribute("hostDto") HostDto hostDto,
                               @RequestParam("useGuide") String useGuide){
        if(useGuide == null || useGuide.trim().isEmpty()){
            return "redirect:/host/useGuide?error"; // 빈 값이면 다시 입력
        }

        hostDto.setUseGuide(useGuide);
        System.out.println("📌 [숙소 이용 가이드 저장 완료]: " + hostDto.getUseGuide());

        return  "redirect:/host/finish-setup";
    }

    @GetMapping("/finish-setup")
    public String finish(){
        return "host-pages/finish-setup";
    }

    @GetMapping("/price")
    public String roomsPrice(){
        return "host-pages/price";
    }
    @PostMapping("/price/save")
    public String savePrice(@ModelAttribute("hostDto") HostDto hostDto,
                            @RequestParam("price") Integer price){
        if(price == null || price <= 0){
            return "redirect:/host/price?error";
        }

        hostDto.setPrice(price);
        System.out.println("📌 [저장된 숙박 요금]: " + hostDto.getPrice());

        return "redirect:/host/receipt";
    }

    @GetMapping("/receipt")
    public String reviewPage(@ModelAttribute("hostDto") HostDto hostDto, Model model) {
        // ✅ `imageFiles`가 null이면 빈 리스트 전달 (오류 방지)


        return "host-pages/receipt";
    }


    @GetMapping("/publish")
    public String publish(){
        return "host-pages/publish";
    }

    // ✅ 최종 등록 페이지 (숙소 등록 요청)
    @PostMapping("/register")
    public String registerRoom(@ModelAttribute("hostDto") HostDto hostDto){
        List<String> fileUrls = hostDto.getFileUrls();  // HostDto에서 fileUrls를 가져옴


        hostService.insertRoom(hostDto);
        int roomId = hostDto.getRoomId();  // 생성된 roomId를 가져옴
        createHostMapper.insertRoomOptions(hostDto.getRoomId(),hostDto.getOptionIds());
        for (String fileUrl : fileUrls){
            hostService.updateRoomImages(fileUrl, roomId);
        }



        return "redirect:/host/today";
    }
}
