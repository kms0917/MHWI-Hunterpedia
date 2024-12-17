package com.example.hunterpedia.builder;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Pair;

import com.example.hunterpedia.R;
import com.example.hunterpedia.api.ApiClient;
import com.example.hunterpedia.api.ApiService;
import com.example.hunterpedia.datastructure.Armor;
import com.example.hunterpedia.datastructure.Charm;
import com.example.hunterpedia.datastructure.Decoration;
import com.example.hunterpedia.datastructure.Skill;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuilderActivity extends AppCompatActivity implements OnSkillSelectedListener {

    private ExpandableListView expandableListView;
    private TextView targetSkillView;
    private Button searchBtn;
    private TextView resultView;
    private LinearLayout loadingLayout;
    private ArrayList<Pair<String, Integer>> targetSkills = new ArrayList<>();
    private ArrayList<Pair<String, Integer>> inputSkills = new ArrayList<>();
    private List<Skill> skills;
    private List<Armor> headArmors;
    private List<Armor> chestArmors;
    private List<Armor> glovesArmors;
    private List<Armor> waistArmors;
    private List<Armor> legsArmors;
    private int totalApiCalls = 6; // API 호출 개수
    private int completedApiCalls = 0; // 완료된 API 호출 개수

    @Override
    public void onSkillSelected(SelectedSkill skill, int selectedLevel) {
        boolean skillExists = false;
        // 기존 스킬이 있는지 확인
        for (Pair<String, Integer> skillData : inputSkills) {
            if (skillData.first.equals(skill.getName())) {
                if (selectedLevel == 0) {
                    // "없음"을 선택한 경우, 리스트에서 제거
                    inputSkills.remove(skillData);
                } else {
                    // 이미 존재하는 스킬이면 레벨을 업데이트
                    inputSkills.set(inputSkills.indexOf(skillData), new Pair<>(skill.getName(), selectedLevel));
                }
                skillExists = true;
                break;
            }
        }

        if (!skillExists && selectedLevel > 0) {
            inputSkills.add(new Pair<>(skill.getName(), selectedLevel));  // 스킬의 이름과 요구 레벨을 저장
        }

        StringBuilder skillsText = new StringBuilder("Selected Skills:\n");
        for (Pair<String, Integer> skillData : inputSkills) {
            skillsText.append(skillData.first)
                    .append(" (Level: ")
                    .append(skillData.second)
                    .append(")\n");
        }

// 텍스트 업데이트
        targetSkillView.setText(skillsText.toString().trim());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.builder_activity); // 레이아웃 파일 설정

        expandableListView = findViewById(R.id.expandableListView);
        targetSkillView = findViewById(R.id.targetSkill);
        searchBtn = findViewById(R.id.search);
        loadingLayout = findViewById(R.id.loadingLayout);

        showLoadingScreen(true); // 로딩 화면 표시
        targetSkillView.setText("Selected Skills:");

        getSkillData();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetSkills.clear();
                for (Pair<String, Integer> pair : inputSkills) {
                    targetSkills.add(new Pair<>(pair.first, pair.second));
                }
                searchArmors();
            }
        });

        getData();
    }


    // ExpandableListView 데이터 초기화
    private void initializeSkillList() {
        List<String> groupList = new ArrayList<>();
        Map<String, List<SelectedSkill>> groupSkillsMap = new HashMap<>();

        // 그룹 항목 추가
        groupList.add("Attack");
        groupList.add("Defence");
        groupList.add("Critical");
        groupList.add("Elemental/Ailment");
        groupList.add("Stamina");
        groupList.add("Sharpness");
        groupList.add("Resistance");
        groupList.add("Evade/Guard");
        groupList.add("Item");
        groupList.add("Combat Assistance");
        groupList.add("Gunner");
        groupList.add("Other Enhancements");

        List<SelectedSkill> attackSkills = new ArrayList<>();
        List<SelectedSkill> defenceSkills = new ArrayList<>();
        List<SelectedSkill> critSkills = new ArrayList<>();
        List<SelectedSkill> affinitySkills = new ArrayList<>();
        List<SelectedSkill> staminaSkills = new ArrayList<>();
        List<SelectedSkill> sharpnessSkills = new ArrayList<>();
        List<SelectedSkill> resistenceSkills = new ArrayList<>();
        List<SelectedSkill> evadeSkills = new ArrayList<>();
        List<SelectedSkill> itemSkills = new ArrayList<>();
        List<SelectedSkill> combatAssistanceSkills = new ArrayList<>();
        List<SelectedSkill> gunnerSkills = new ArrayList<>();
        List<SelectedSkill> othersSkills = new ArrayList<>();

        Map<Integer, List<SelectedSkill>> skillCategoryMap = new HashMap<>();

        skillCategoryMap.put(1, resistenceSkills);  // Poison Resistance
        skillCategoryMap.put(2, resistenceSkills);  // Paralysis Resistance
        skillCategoryMap.put(3, resistenceSkills);  // Sleep Resistance
        skillCategoryMap.put(4, resistenceSkills);  // Stun Resistance
        skillCategoryMap.put(5, resistenceSkills);  // Muck Resistance
        skillCategoryMap.put(6, resistenceSkills);  // Blast Resistance
        skillCategoryMap.put(7, resistenceSkills);  // Bleeding Resistance
        skillCategoryMap.put(8, resistenceSkills);  // Iron Skin
        skillCategoryMap.put(9, evadeSkills);      // Earplugs
        skillCategoryMap.put(10, evadeSkills);     // Windproof
        skillCategoryMap.put(11, evadeSkills);     // Tremor Resistance
        skillCategoryMap.put(12, itemSkills);      // Dungmaster
        skillCategoryMap.put(13, itemSkills);      // Effluvial Expert
        skillCategoryMap.put(14, itemSkills);      // Heat Guard
        skillCategoryMap.put(15, attackSkills);    // Attack Boost
        skillCategoryMap.put(16, defenceSkills);   // Defense Boost
        skillCategoryMap.put(17, combatAssistanceSkills);    // Health Boost
        skillCategoryMap.put(18, combatAssistanceSkills);    // Recovery Up
        skillCategoryMap.put(19, combatAssistanceSkills);    // Recovery Speed
        skillCategoryMap.put(20, resistenceSkills); // Fire Resistance
        skillCategoryMap.put(21, resistenceSkills); // Water Resistance
        skillCategoryMap.put(22, resistenceSkills); // Ice Resistance
        skillCategoryMap.put(23, resistenceSkills); // Thunder Resistance
        skillCategoryMap.put(24, resistenceSkills); // Dragon Resistance
        skillCategoryMap.put(25, resistenceSkills); // Blight Resistance
        skillCategoryMap.put(26, affinitySkills);  // Fire Attack
        skillCategoryMap.put(27, affinitySkills);  // Water Attack
        skillCategoryMap.put(28, affinitySkills);  // Ice Attack
        skillCategoryMap.put(29, affinitySkills);  // Thunder Attack
        skillCategoryMap.put(30, affinitySkills);
        skillCategoryMap.put(31, affinitySkills);
        skillCategoryMap.put(32, affinitySkills);
        skillCategoryMap.put(33, affinitySkills);
        skillCategoryMap.put(34, affinitySkills);
        skillCategoryMap.put(35, gunnerSkills);
        skillCategoryMap.put(36, gunnerSkills);
        skillCategoryMap.put(37, gunnerSkills);
        skillCategoryMap.put(38, gunnerSkills);
        skillCategoryMap.put(39, critSkills);
        skillCategoryMap.put(40, critSkills);
        skillCategoryMap.put(41, critSkills);
        skillCategoryMap.put(42, combatAssistanceSkills);
        skillCategoryMap.put(43, combatAssistanceSkills);
        skillCategoryMap.put(44, sharpnessSkills);
        skillCategoryMap.put(45, critSkills);
        skillCategoryMap.put(46, combatAssistanceSkills); // Name: Partbreaker, Id: 46, Description: Makes it easier to break or sever parts of large monsters., rank: 3
        skillCategoryMap.put(47, combatAssistanceSkills); // Name: Slugger, Id: 47, Description: Makes it easier to stun monsters., rank: 5
        skillCategoryMap.put(48, affinitySkills); // Name: Stamina Thief, Id: 48, Description: Increases certain attacks' ability to exhaust monsters., rank: 5
        skillCategoryMap.put(49, othersSkills); // Name: Master Mounter, Id: 49, Description: Makes it easier to mount monsters., rank: 1
        skillCategoryMap.put(50, othersSkills); // Name: Airborne, Id: 50, Description: Increases the damage caused by jumping attacks., rank: 1
        skillCategoryMap.put(51, critSkills); // Name: Latent Power, Id: 51, Description: Temporarily increases affinity and reduces stamina depletion when certain conditions are met., rank: 7
        skillCategoryMap.put(52, attackSkills); // Name: Agitator, Id: 52, Description: Increases attack power and affinity when large monsters become enraged., rank: 7
        skillCategoryMap.put(53, attackSkills); // Name: Peak Performance, Id: 53, Description: Increases attack when your health is full., rank: 3
        skillCategoryMap.put(54, attackSkills); // Name: Heroics, Id: 54, Description: Increases attack power and defense when health drops to 35% or lower., rank: 7
        skillCategoryMap.put(55, attackSkills); // Name: Fortify, Id: 55, Description: Temporarily increases your attack and defense every time you faint up to 2 times., rank: 1
        skillCategoryMap.put(56, attackSkills); // Name: Resentment, Id: 56, Description: Increases attack when you have recoverable damage (the red portion of your health gauge)., rank: 5
        skillCategoryMap.put(57, evadeSkills); // Name: Resuscitate, Id: 57, Description: Improves evasion and reduces stamina depletion when afflicted with abnormal status effects., rank: 1
        skillCategoryMap.put(58, combatAssistanceSkills); // Name: Horn Maestro, Id: 58, Description: Increases the effect duration of hunting horn melodies., rank: 2
        skillCategoryMap.put(59, combatAssistanceSkills); // Name: Capacity Boost, Id: 59, Description: Increases the gunlance's shell capacity and charge blade's phial capacity., rank: 1
        skillCategoryMap.put(60, gunnerSkills); // Name: Special Ammo Boost, Id: 60, Description: Increases the power of bowgun special ammo and Dragon Piercer., rank: 2
        skillCategoryMap.put(61, combatAssistanceSkills); // Name: Artillery, Id: 61, Description: Strengthens explosive attacks like gunlance shells, Wyvern's Fire, charge blade phial attacks, and sticky ammo., rank: 5
        skillCategoryMap.put(62, othersSkills); // Name: Heavy Artillery, Id: 62, Description: Increases the firepower of ballistae and cannons., rank: 2
        skillCategoryMap.put(63, staminaSkills); // Name: Marathon Runner, Id: 63, Description: Slows down stamina depletion for actions which continuously drain stamina (such as dashing)., rank: 3
        skillCategoryMap.put(64, staminaSkills); // Name: Constitution, Id: 64, Description: Reduces stamina depletion when performing stamina-draining moves such as evading, etc., rank: 5
        skillCategoryMap.put(65, evadeSkills); // Name: Leap of Faith, Id: 65, Description: Allows you to do a dive-evade when facing towards large monsters, and extends the dive-evade distance., rank: 1
        skillCategoryMap.put(66, staminaSkills); // Name: Stamina Surge, Id: 66, Description: Speeds up stamina recovery., rank: 3
        skillCategoryMap.put(67, staminaSkills); // Name: Hunger Resistance, Id: 67, Description: Reduces maximum stamina depletion over time. However, does not work against cold environments that reduce stamina., rank: 3
        skillCategoryMap.put(68, evadeSkills); // Name: Evade Window, Id: 68, Description: Extends the invulnerability period when evading., rank: 5
        skillCategoryMap.put(69, evadeSkills); // Name: Evade Extender, Id: 69, Description: Increases evade distance., rank: 3
        skillCategoryMap.put(70, evadeSkills); // Name: Guard, Id: 70, Description: Reduces knockbacks and stamina depletion when guarding., rank: 5
        skillCategoryMap.put(71, combatAssistanceSkills); // Name: Quick Sheath, Id: 71, Description: Speeds up weapon sheathing., rank: 3
        skillCategoryMap.put(72, combatAssistanceSkills); // Name: Wide-Range, Id: 72, Description: Allows the effects of certain items to also affect nearby allies., rank: 5
        skillCategoryMap.put(73, itemSkills); // Name: Item Prolonger, Id: 73, Description: Extends the duration of some item effects., rank: 3
        skillCategoryMap.put(74, itemSkills); // Name: Free Meal, Id: 74, Description: Gives you a predetermined chance of consuming a food or drink item for free., rank: 3
        skillCategoryMap.put(75, itemSkills); // Name: Speed Eating, Id: 75, Description: Increases meat-eating and item-consumption speed., rank: 3
        skillCategoryMap.put(76, sharpnessSkills); // Name: Speed Sharpening, Id: 76, Description: Speeds up weapon sharpening when using a whetstone., rank: 3
        skillCategoryMap.put(77, itemSkills); // Name: Bombardier, Id: 77, Description: Increases the damage of explosive items., rank: 5
        skillCategoryMap.put(78, itemSkills); // Name: Mushroomancer, Id: 78, Description: Lets you digest mushrooms that would otherwise be inedible and gain their advantageous effects., rank: 3
        skillCategoryMap.put(79, othersSkills); // Name: Master Fisher, Id: 79, Description: Improves your skill at fishing., rank: 1
        skillCategoryMap.put(80, othersSkills); // Name: Pro Transporter, Id: 80, Description: Increases your speed while transporting items and decreases the likelihood of dropping them., rank: 1
        skillCategoryMap.put(81, othersSkills); // Name: Master Gatherer, Id: 81, Description: Allows you to gather more quickly, and also prevents attacks from knocking you back while gathering., rank: 1
        skillCategoryMap.put(82, othersSkills); // Name: Honey Hunter, Id: 82, Description: Increases the quantity of honey you receive when gathering in the field., rank: 1
        skillCategoryMap.put(83, othersSkills); // Name: Carving Pro, Id: 83, Description: Prevents knockback from attacks while carving., rank: 1
        skillCategoryMap.put(84, defenceSkills); // Name: Divine Blessing, Id: 84, Description: Has a predetermined chance of reducing the damage you take., rank: 5
        skillCategoryMap.put(85, othersSkills); // Name: Palico Rally, Id: 85, Description: Powers up Palicoes., rank: 5
        skillCategoryMap.put(86, itemSkills); // Name: Botanist, Id: 86, Description: Increases the quantity of herbs and other consumable items you gather., rank: 4
        skillCategoryMap.put(87, itemSkills); // Name: Geologist, Id: 87, Description: Increases the number of times you can use a gathering point., rank: 3
        skillCategoryMap.put(88, affinitySkills); // Name: Maximum Might, Id: 88, Description: Increases affinity as long as stamina is kept full for a period of time., rank: 5
        skillCategoryMap.put(89, othersSkills); // Name: Slinger Capacity, Id: 89, Description: Increases the loading capacity for slinger ammo obtained in the field., rank: 5
        skillCategoryMap.put(90, othersSkills); // Name: Stealth, Id: 90, Description: Makes it easier for monsters to lose sight of you., rank: 3
        skillCategoryMap.put(91, combatAssistanceSkills); // Name: Flinch Free, Id: 91, Description: Prevents knockbacks and other reactions to small damage., rank: 3
        skillCategoryMap.put(92, othersSkills); // Name: Scoutfly Range Up, Id: 92, Description: Expands your scoutflies' detection range., rank: 1
        skillCategoryMap.put(93, othersSkills); // Name: Speed Crawler, Id: 93, Description: Increases movement speed while crouching., rank: 1
        skillCategoryMap.put(94, othersSkills); // Name: Jump Master, Id: 94, Description: Prevents attacks from knocking you back during a jump., rank: 1
        skillCategoryMap.put(95, othersSkills); // Name: Survival Expert, Id: 95, Description: Extra health is recovered from environmental interactables like Sporepuffs or Wiggly Litchi., rank: 3
        skillCategoryMap.put(96, othersSkills); // Name: Aquatic/Polar Mobility, Id: 96, Description: Lets you move more freely in water. Also prevents you from being slowed down by terrain effects, such as deep snow., rank: 3
        skillCategoryMap.put(97, othersSkills); // Name: Cliffhanger, Id: 97, Description: Decreases stamina depletion when evading while clinging to walls or ivy, or when moving while grappling onto an enemy., rank: 1
        skillCategoryMap.put(98, itemSkills); // Name: Blindsider, Id: 98, Description: Improves the effectiveness of flash attacks and items., rank: 1
        skillCategoryMap.put(99, othersSkills); // Name: Scholar, Id: 99, Description: Speeds up progress on research levels and special investigations. (Effect is applied at the quest results screen.), rank: 1
        skillCategoryMap.put(100, othersSkills); // Name: Entomologist, Id: 100, Description: Decreases the chances of destroying the bodies of small insect monsters, allowing them to be carved., rank: 3
        skillCategoryMap.put(101, resistenceSkills); // Name: Effluvia Resistance, Id: 101, Description: Gain a resistance to effluvial buildup., rank: 3
        skillCategoryMap.put(102, othersSkills); // Name: Scenthound, Id: 102, Description: Increases your scoutflies' gauge fill rate when you find tracks and other traces left by monsters., rank: 1
        skillCategoryMap.put(103, othersSkills); // Name: Forager's Luck, Id: 103, Description: Increases the chance of finding rare gathering points., rank: 1
        skillCategoryMap.put(104, othersSkills); // Name: Detector, Id: 104, Description: Shows rare gathering points on the Wildlife Map., rank: 1
        skillCategoryMap.put(105, othersSkills); // Name: BBQ Master, Id: 105, Description: Improves your skill at cooking meat., rank: 1
        skillCategoryMap.put(106, itemSkills); // Name: Tool Specialist, Id: 106, Description: Reduces the recharge time for specialized tools., rank: 5
        skillCategoryMap.put(107, affinitySkills); // Name: Affinity Sliding, Id: 107, Description: Sliding increases your affinity for a short time., rank: 1
        skillCategoryMap.put(108, othersSkills); // Name: Intimidator, Id: 108, Description: Reduces the chances small monsters will attack after spotting you. (No effect on certain monsters.), rank: 3
        skillCategoryMap.put(109, null);
        skillCategoryMap.put(110, null);
        skillCategoryMap.put(111, null);
        skillCategoryMap.put(112, null);
        skillCategoryMap.put(113, null);
        skillCategoryMap.put(114, null);
        skillCategoryMap.put(115, null);
        skillCategoryMap.put(116, null);
        skillCategoryMap.put(117, null);
        skillCategoryMap.put(118, null);
        skillCategoryMap.put(119, null);
        skillCategoryMap.put(120, null);
        skillCategoryMap.put(121, null);
        skillCategoryMap.put(122, null);
        skillCategoryMap.put(123, null);
        skillCategoryMap.put(124, null);
        skillCategoryMap.put(125, null);
        skillCategoryMap.put(126, null);
        skillCategoryMap.put(127, null);
        skillCategoryMap.put(128, null);
        skillCategoryMap.put(129, gunnerSkills);
        skillCategoryMap.put(130, gunnerSkills);
        skillCategoryMap.put(131, gunnerSkills);
        skillCategoryMap.put(132, gunnerSkills);
        skillCategoryMap.put(133, othersSkills);
        skillCategoryMap.put(134, null);
        skillCategoryMap.put(135, null);
        skillCategoryMap.put(136, null);
        skillCategoryMap.put(137, null);
        skillCategoryMap.put(138, evadeSkills);
        skillCategoryMap.put(139, null);
        skillCategoryMap.put(140, null);
        skillCategoryMap.put(141, null);
        skillCategoryMap.put(142, null);
        skillCategoryMap.put(143, null);
        skillCategoryMap.put(144, null);
        skillCategoryMap.put(145, null);
        skillCategoryMap.put(146, attackSkills);
        skillCategoryMap.put(147, null);
        skillCategoryMap.put(148, null);
        skillCategoryMap.put(149, null);
        skillCategoryMap.put(150, null);
        skillCategoryMap.put(151, null);
        skillCategoryMap.put(152, null);
        skillCategoryMap.put(153, null);
        skillCategoryMap.put(154, null);
        skillCategoryMap.put(155, null);
        skillCategoryMap.put(156, null);
        skillCategoryMap.put(157, null);
        skillCategoryMap.put(158, null);
        skillCategoryMap.put(159, null);
        skillCategoryMap.put(160, othersSkills);
        skillCategoryMap.put(161, othersSkills);
        skillCategoryMap.put(162, resistenceSkills);
        skillCategoryMap.put(163, null);
        skillCategoryMap.put(164, null);
        skillCategoryMap.put(165, null);
        skillCategoryMap.put(166, null);
        skillCategoryMap.put(167, null);
        skillCategoryMap.put(168, null);
        skillCategoryMap.put(169, null);
        skillCategoryMap.put(170, null);
        skillCategoryMap.put(171, null);
        skillCategoryMap.put(172, null);
        skillCategoryMap.put(173, null);
        skillCategoryMap.put(174, null);
        skillCategoryMap.put(175, null);
        skillCategoryMap.put(176, null);
        skillCategoryMap.put(177, othersSkills);
        skillCategoryMap.put(178, null);
        skillCategoryMap.put(179, null);
        skillCategoryMap.put(180, null);
        skillCategoryMap.put(181, null);


        for (int i = 0; i < 108; i++) {
            Skill skill = skills.get(i);
            List<SelectedSkill> targetList = skillCategoryMap.get(skill.getId());
            if (targetList != null) {
                targetList.add(new SelectedSkill(skill.getName(), createSkillOptions(skill.getRanks().size()), skill.getRanks().size()));
            }
        }

        // 각 그룹에 스킬 리스트 추가
        groupSkillsMap.put(groupList.get(0), attackSkills);
        groupSkillsMap.put(groupList.get(1), defenceSkills);
        groupSkillsMap.put(groupList.get(2), critSkills);
        groupSkillsMap.put(groupList.get(3), affinitySkills);
        groupSkillsMap.put(groupList.get(4), staminaSkills);
        groupSkillsMap.put(groupList.get(5), sharpnessSkills);
        groupSkillsMap.put(groupList.get(6), resistenceSkills);
        groupSkillsMap.put(groupList.get(7), evadeSkills);
        groupSkillsMap.put(groupList.get(8), itemSkills);
        groupSkillsMap.put(groupList.get(9), combatAssistanceSkills);
        groupSkillsMap.put(groupList.get(10), gunnerSkills);
        groupSkillsMap.put(groupList.get(11), othersSkills);

        ExpandableListAdapter expandableListAdapter = new SkillListAdapter(BuilderActivity.this, groupList, groupSkillsMap, this);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void getSkillData() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Skill>> call = apiService.getSkills();

        call.enqueue(new Callback<List<Skill>>() {
            @Override
            public void onResponse(@NonNull Call<List<Skill>> call, @NonNull Response<List<Skill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 스킬 데이터가 성공적으로 받아졌을 때
                    skills = response.body();
                    // 받은 스킬 데이터를 처리하는 코드 작성
                    Log.d("ArmorBuilder", "Skills loaded: " + skills.size());
                    initializeSkillList();
                    checkAllApiCompleted();
                } else {
                    // API 호출이 실패한 경우
                    Log.e("ArmorBuilder", "Failed to load skills.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
                // 네트워크 오류 등으로 실패한 경우
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                checkAllApiCompleted();
            }
        });
    }

    private void getData() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        // Fetch Head Armors
        Call<List<Armor>> headCall = apiService.getHeadArmors();
        headCall.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Armor>> call, @NonNull Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    headArmors = response.body();
                    Collections.reverse(headArmors); // Optional: reverse list if needed
                    Log.d("ArmorBuilder", "Head armors loaded: " + headArmors.size());
                    checkAllApiCompleted();
                } else {
                    Log.e("ArmorBuilder", "Failed to load head armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                checkAllApiCompleted();
            }
        });

// Fetch Chest Armors
        Call<List<Armor>> chestCall = apiService.getChestArmors();
        chestCall.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Armor>> call, @NonNull Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chestArmors = response.body();
                    Collections.reverse(chestArmors); // Optional: reverse list if needed
                    Log.d("ArmorBuilder", "Chest armors loaded: " + chestArmors.size());
                    checkAllApiCompleted();
                } else {
                    Log.e("ArmorBuilder", "Failed to load chest armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                checkAllApiCompleted();
            }
        });

// Fetch Gloves Armors
        Call<List<Armor>> glovesCall = apiService.getGlovesArmors();
        glovesCall.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Armor>> call, @NonNull Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    glovesArmors = response.body();
                    Collections.reverse(glovesArmors); // Optional: reverse list if needed
                    Log.d("ArmorBuilder", "Gloves armors loaded: " + glovesArmors.size());
                    checkAllApiCompleted();
                } else {
                    Log.e("ArmorBuilder", "Failed to load gloves armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                checkAllApiCompleted();
            }
        });

// Fetch Waist Armors
        Call<List<Armor>> waistCall = apiService.getWaistArmors();
        waistCall.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Armor>> call, @NonNull Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    waistArmors = response.body();
                    Collections.reverse(waistArmors); // Optional: reverse list if needed
                    Log.d("ArmorBuilder", "Waist armors loaded: " + waistArmors.size());
                    checkAllApiCompleted();
                } else {
                    Log.e("ArmorBuilder", "Failed to load waist armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                checkAllApiCompleted();
            }
        });

// Fetch Legs Armors
        Call<List<Armor>> legsCall = apiService.getLegsArmors();
        legsCall.enqueue(new Callback<List<Armor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Armor>> call, @NonNull Response<List<Armor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    legsArmors = response.body();
                    Collections.reverse(legsArmors); // Optional: reverse list if needed
                    Log.d("ArmorBuilder", "Legs armors loaded: " + legsArmors.size());
                    checkAllApiCompleted();
                } else {
                    Log.e("ArmorBuilder", "Failed to load legs armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
                checkAllApiCompleted();
            }
        });
    }

    private void checkAllApiCompleted() {
        completedApiCalls++;
        if (completedApiCalls == totalApiCalls) {
            showLoadingScreen(false); // 모든 API 호출이 완료되면 로딩 화면 숨김
        }
    }

    private void searchArmors() {
        HashMap<String, List<String>> resultMap = new HashMap<>();
        List<String> groupList = Arrays.asList("Head Armors", "Head Armors with Decoration", "Chest Armors", "Chest Armors with Decoration", "Gloves Armors", "Gloves Armors with Decoration", "Waist Armors", "Waist Armors with Decoration", "Legs Armors", "Legs Armors with Decoration");

        resultMap.put("Head Armors", formatArmorResults(filteringHeads(headArmors, 1, 4, false)));
        resultMap.put("Head Armors with Decoration", formatArmorResults(filteringHeads(headArmors, 1, 4, true)));
        resultMap.put("Chest Armors", formatArmorResults(filteringChests(chestArmors, 1, 4, false)));
        resultMap.put("Chest Armors with Decoration", formatArmorResults(filteringChests(chestArmors, 1, 4, true)));
        resultMap.put("Gloves Armors", formatArmorResults(filteringGloves(glovesArmors, 1, 4, false)));
        resultMap.put("Gloves Armors with Decoration", formatArmorResults(filteringGloves(glovesArmors, 1, 4, true)));
        resultMap.put("Waist Armors", formatArmorResults(filteringWaists(waistArmors, 1, 4, false)));
        resultMap.put("Waist Armors with Decoration", formatArmorResults(filteringWaists(waistArmors, 1, 4, true)));
        resultMap.put("Legs Armors", formatArmorResults(filteringLegs(legsArmors, 1, 4, false)));
        resultMap.put("Legs Armors with Decoration", formatArmorResults(filteringLegs(legsArmors, 1, 4, true)));

        ExpandableListView resultListView = findViewById(R.id.resultExpandableList);
        ExpandableListAdapter adapter = new CustomExpandableListAdapter(this, groupList, resultMap);
        resultListView.setAdapter(adapter);
    }


    private List<String> formatArmorResults(List<Pair<Armor, Integer>> filteredArmors) {
        List<String> results = new ArrayList<>();
        int prevScore = -1, count = 0;

        for (Pair<Armor, Integer> armorPair : filteredArmors) {
            if (count >= 2) break; // 상위 2개의 점수만 사용
            if (armorPair.second != prevScore) count++; // 새로운 점수 감지
            prevScore = armorPair.second;

            Armor armor = armorPair.first;

            // HTML 형식을 이용해 'Name'을 강조체로 설정하고, 나머지는 일반 텍스트로 설정
            String formattedText = "<font color='#0000FF'><b style='font-size:24sp;'>Name: " + armor.getName() + "</b></font>" +
                    "<br>Skills: " + getSkillDescription(armor) +
                    "<br>Slots: " + getSlotDescription(armor);

            results.add(formattedText);
        }
        return results;
    }



    private String getSkillDescription(Armor armor) {
        StringBuilder skills = new StringBuilder();
        for (Skill.Rank skill : armor.getSkills()) {
            skills.append(skill.getSkillName()).append("(Lv").append(skill.getLevel()).append("), ");
        }
        return skills.toString();
    }

    private String getSlotDescription(Armor armor) {
        StringBuilder slots = new StringBuilder();
        for (Armor.Slot slot : armor.getSlots()) {
            slots.append("Lv").append(slot.getRank()).append(" ");
        }
        return slots.toString().trim();
    }


    private List<Pair<Armor, Integer>> filteringHeads(List<Armor> headArmors, int slotMin, int slotMax, boolean withDeco){
        List<Pair<Armor, Integer>> filteredHead = new ArrayList<>();
        for (Armor armor : headArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax, withDeco);
            filteredHead.add(new Pair<> (armor, score));
        }
        filteredHead.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredHead;
    }

    private List<Pair<Armor, Integer>> filteringChests(List<Armor> chestArmors, int slotMin, int slotMax, boolean withDeco){
        List<Pair<Armor, Integer>> filteredChest = new ArrayList<>();
        for (Armor armor : chestArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax, withDeco);
            filteredChest.add(new Pair<> (armor, score));
        }
        filteredChest.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredChest;
    }

    private List<Pair<Armor, Integer>> filteringGloves(List<Armor> glovesArmors, int slotMin, int slotMax, boolean withDeco){
        List<Pair<Armor, Integer>> filteredGloves = new ArrayList<>();
        for (Armor armor : glovesArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax, withDeco);
            filteredGloves.add(new Pair<> (armor, score));
        }
        filteredGloves.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredGloves;
    }

    private List<Pair<Armor, Integer>> filteringWaists(List<Armor> waistArmors, int slotMin, int slotMax, boolean withDeco){
        List<Pair<Armor, Integer>> filteredWaist = new ArrayList<>();
        for (Armor armor : waistArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax, withDeco);
            filteredWaist.add(new Pair<> (armor, score));
        }
        filteredWaist.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredWaist;
    }

    private List<Pair<Armor, Integer>> filteringLegs(List<Armor> legsArmors, int slotMin, int slotMax, boolean withDeco){
        List<Pair<Armor, Integer>> filteredLegs = new ArrayList<>();
        for (Armor armor : legsArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax, withDeco);
            filteredLegs.add(new Pair<> (armor, score));
        }
        filteredLegs.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredLegs;
    }

    // 장비에 달린 스킬 점수화
    private int calculateSkillScore(Armor armor) {
        int skillScore = 0;

        for (Skill.Rank skill : armor.getSkills()) {
            for (Pair<String, Integer> targetSkill : targetSkills) {
                if (skill.getSkillName().equals(targetSkill.first)) {
                    int skillLevel = skill.getLevel();
                    int targetLevel = targetSkill.second;
                    if (skillLevel >= targetLevel) {
                        skillScore += targetLevel;  // 목표 스킬 레벨 만큼 점수 추가
                    } else {
                        skillScore += skillLevel;  // 목표 레벨 이하일 경우 실제 레벨만큼 점수 추가
                    }
                }
            }
        }
        return skillScore;
    }
    // 장비의 슬롯 크기 점수화
    private int calculateSlotScore(Armor armor, int slotMin, int slotMax, boolean withDeco) {
        int slotScore = 0;
        // 4레벨 슬롯은 무조건 +2점
        for (Armor.Slot slot : armor.getSlots()) {
            if (slot.getRank() == 4 && slotMax == 4 && withDeco) {
                slotScore += 2;
            }
            else if (slot.getRank() >= slotMin) {
                slotScore += 1;
            }
        }
        return slotScore;
    }

    private List<String> createSkillOptions(int maxLevel) {
        List<String> options = new ArrayList<>();
        options.add("없음");
        for (int i = 1; i <= maxLevel; i++) {
            options.add("Level " + i);
        }
        return options;
    }

    private void showLoadingScreen(boolean show) {
        if (show) {
            loadingLayout.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
        }
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> groupList;
        private HashMap<String, List<String>> childMap;

        public CustomExpandableListAdapter(Context context, List<String> groupList, HashMap<String, List<String>> childMap) {
            this.context = context;
            this.groupList = groupList;
            this.childMap = childMap;
        }

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childMap.get(groupList.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childMap.get(groupList.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String groupTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(groupTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(Html.fromHtml(childText));
            return convertView;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


}