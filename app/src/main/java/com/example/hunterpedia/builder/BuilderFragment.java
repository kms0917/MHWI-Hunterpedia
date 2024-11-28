package com.example.hunterpedia.builder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

public class BuilderFragment extends Fragment implements OnSkillSelectedListener {

    private Spinner weaponSpinner;
    private List<String> weaponSlots;
    private ExpandableListView expandableListView;
    private TextView targetSkillView;
    private Button searchBtn;
    private TextView resultView;
    private ArrayList<Pair<String, Integer>> targetSkills = new ArrayList<>();
    private List<Skill> skills;
    private List<Armor> headArmors;
    private List<Armor> chestArmors;
    private List<Armor> glovesArmors;
    private List<Armor> waistArmors;
    private List<Armor> legsArmors;
    private List<Decoration> decorations;
    private List<Charm> charms;

    @Override
    public void onSkillSelected(SelectedSkill skill, int selectedLevel) {
        boolean skillExists = false;
        // 기존 스킬이 있는지 확인
        for (Pair<String, Integer> skillData : targetSkills) {
            if (skillData.first.equals(skill.getName())) {
                if (selectedLevel == 0) {
                    // "없음"을 선택한 경우, 리스트에서 제거
                    targetSkills.remove(skillData);
                } else {
                    // 이미 존재하는 스킬이면 레벨을 업데이트
                    targetSkills.set(targetSkills.indexOf(skillData), new Pair<>(skill.getName(), selectedLevel));
                }
                skillExists = true;
                break;
            }
        }

        if (!skillExists && selectedLevel > 0) {
            targetSkills.add(new Pair<>(skill.getName(), selectedLevel));  // 스킬의 이름과 요구 레벨을 저장
        }

        StringBuilder skillsText = new StringBuilder("Selected Skills:\n");
        for (Pair<String, Integer> skillData : targetSkills) {
            skillsText.append(skillData.first)
                    .append(" (Level: ")
                    .append(skillData.second)
                    .append(")\n");
        }

// 텍스트 업데이트
        targetSkillView.setText(skillsText.toString().trim());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.builder_fragment, container, false);

        weaponSpinner = view.findViewById(R.id.weaponspinner);
        expandableListView = view.findViewById(R.id.expandableListView);
        targetSkillView = view.findViewById(R.id.targetSkill);
        searchBtn = view.findViewById(R.id.search);
        resultView = view.findViewById(R.id.testresult);

        getSkillData();
        initializeWeaponSlots();

        ArrayAdapter<String> weaponAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, weaponSlots);
        weaponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weaponSpinner.setAdapter(weaponAdapter);

        weaponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedWeapon = weaponSlots.get(position);
                int[] slotValues = parseSlotValues(selectedWeapon);
                Toast.makeText(requireContext(), "Weapon Slots: " + slotValues[0] + ", " + slotValues[1] + ", " + slotValues[2], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArmors();
            }
        });

        weaponSpinner.setSelection(0);
        getData();
        return view;
    }

    // Weapon Spinner 데이터 초기화
    private void initializeWeaponSlots() {
        weaponSlots = new ArrayList<>();
        weaponSlots.add("없음");
        weaponSlots.add("1");
        weaponSlots.add("1, 1");
        weaponSlots.add("1, 1, 1");
        weaponSlots.add("2");
        weaponSlots.add("2, 1");
        weaponSlots.add("2, 2");
        weaponSlots.add("2, 1, 1");
        weaponSlots.add("2, 2, 1");
        weaponSlots.add("2, 2, 2");
        weaponSlots.add("3");
        weaponSlots.add("3, 1");
        weaponSlots.add("3, 2");
        weaponSlots.add("3, 3");
        weaponSlots.add("3, 1, 1");
        weaponSlots.add("3, 2, 1");
        weaponSlots.add("3, 2, 2");
        weaponSlots.add("3, 3, 1");
        weaponSlots.add("3, 3, 2");
        weaponSlots.add("3, 3, 3");
        weaponSlots.add("4");
        weaponSlots.add("4, 1");
        weaponSlots.add("4, 2");
        weaponSlots.add("4, 3");
        weaponSlots.add("4, 4");
        weaponSlots.add("4, 1, 1");
        weaponSlots.add("4, 2, 1");
        weaponSlots.add("4, 2, 2");
        weaponSlots.add("4, 3, 1");
        weaponSlots.add("4, 3, 2");
        weaponSlots.add("4, 3, 3");
        weaponSlots.add("4, 4, 1");
        weaponSlots.add("4, 4, 2");
        weaponSlots.add("4, 4, 3");
        weaponSlots.add("4, 4, 4");
    }

    // ExpandableListView 데이터 초기화
    private void initializeSkillList() {
        List<String> groupList = new ArrayList<>();
        Map<String, List<SelectedSkill>> groupSkillsMap = new HashMap<>();

        // 그룹 항목 추가
        groupList.add("공격력 상승");
        groupList.add("방어 계열");
        groupList.add("회심률 계열");
        groupList.add("속성/상태이상 강화 계열");
        groupList.add("스태미나 계열");
        groupList.add("예리도 계열");
        groupList.add("속성/상태이상 내성");
        groupList.add("회피/가드 계열");
        groupList.add("아이템 사용 계열");
        groupList.add("전투 보조 계열");
        groupList.add("건너 전용");
        groupList.add("기타 성능 강화");

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

        ExpandableListAdapter expandableListAdapter = new SkillListAdapter(requireContext(), groupList, groupSkillsMap, this);
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
                } else {
                    // API 호출이 실패한 경우
                    Log.e("ArmorBuilder", "Failed to load skills.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
                // 네트워크 오류 등으로 실패한 경우
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
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
                } else {
                    Log.e("ArmorBuilder", "Failed to load head armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
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
                } else {
                    Log.e("ArmorBuilder", "Failed to load chest armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
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
                } else {
                    Log.e("ArmorBuilder", "Failed to load gloves armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
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
                } else {
                    Log.e("ArmorBuilder", "Failed to load waist armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
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
                } else {
                    Log.e("ArmorBuilder", "Failed to load legs armors.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Armor>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
            }
        });

        Call<List<Decoration>> call2 = apiService.getDecorations();

        call2.enqueue(new Callback<List<Decoration>>() {
            @Override
            public void onResponse(@NonNull Call<List<Decoration>> call, @NonNull Response<List<Decoration>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    decorations = response.body();
                    Log.d("ArmorBuilder", "Decorations loaded: " + decorations.size());
                } else {
                    // API 호출이 실패한 경우
                    Log.e("ArmorBuilder", "Failed to load Decorations.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Decoration>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
            }
        });

        Call<List<Charm>> call3 = apiService.getCharms();

        call3.enqueue(new Callback<List<Charm>>() {
            @Override
            public void onResponse(@NonNull Call<List<Charm>> call, @NonNull Response<List<Charm>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    charms = response.body();
                    Log.d("ArmorBuilder", "Charms loaded: " + charms.size());
                    searchBtn.setVisibility(View.VISIBLE);
                } else {
                    // API 호출이 실패한 경우
                    Log.e("ArmorBuilder", "Failed to load Charms.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Charm>> call, @NonNull Throwable t) {
                Log.e("ArmorBuilder", "Error: " + t.getMessage());
            }
        });
    }

    private void searchArmors() {
        if (targetSkills.isEmpty()){
            resultView.setText("");  // 기존 내용을 지웁니다.
            resultView.append("Please Select Some Skills.\n");
            return;
        }
        //호석 필터링
        List<Pair<Charm, Integer>> filteredCharms = filteringCharms();

// 2. 장식주 필터링: 필요한 스킬만 포함된 장식주 선택
        List<Decoration> filteredDecorations = filteringDeorations();

        int slotMin = 4;
        int slotMax = 0;
        for (Decoration decoration : filteredDecorations) {
            if (decoration.getSlot() < slotMin){
                slotMin = decoration.getSlot();
            }
            if (decoration.getSlot() > slotMax){
                slotMax = decoration.getSlot();
            }
        }

        //시리즈 입력이 있을 시 시리즈 중에서 먼저 장비 선택, 선택 방식은 아래와 같이 평가 후 평가치가 가장 높은 시리즈 장비 선택하는 방식으로

        // Armor 배열: 0=head, 1=chest, 2=gloves, 3=waist, 4=legs
        Armor[] selectedArmors = new Armor[5];
        boolean[] checkSelected = {false, false, false, false, false};  //아무 장비도 선택되지 않았으므로 다 false

        List<Pair<Armor, Integer>> filteredHead = filteringHeads(headArmors, slotMin, slotMax);     //시리즈 입력이 없을때의 상황으로 일단 진행
        List<Pair<Armor, Integer>> filteredChest = filteringChests(chestArmors, slotMin, slotMax);
        List<Pair<Armor, Integer>> filteredGloves = filteringGloves(glovesArmors, slotMin, slotMax);
        List<Pair<Armor, Integer>> filteredWaist = filteringWaists(waistArmors, slotMin, slotMax);
        List<Pair<Armor, Integer>> filteredLegs = filteringLegs(legsArmors, slotMin, slotMax);

        for (int i = 0; i < 5; i++){
            Armor selectedArmor = chooseArmor(filteredHead, filteredChest, filteredGloves, filteredWaist, filteredLegs, checkSelected);
            // 2. 선택된 장비의 부위 인덱스에 저장
            switch (selectedArmor.getType()) {
                case "head":
                    if (selectedArmors[0] == null)
                        selectedArmors[0] = selectedArmor;
                    checkSelected[0] = true;
                    break;
                case "chest":
                    if (selectedArmors[1] == null)
                        selectedArmors[1] = selectedArmor;
                    checkSelected[1] = true;
                    break;
                case "gloves":
                    if (selectedArmors[2] == null)
                        selectedArmors[2] = selectedArmor;
                    checkSelected[2] = true;
                    break;
                case "waist":
                    if (selectedArmors[3] == null)
                        selectedArmors[3] = selectedArmor;
                    checkSelected[3] = true;
                    break;
                case "legs":
                    if (selectedArmors[4] == null)
                        selectedArmors[4] = selectedArmor;
                    checkSelected[4] = true;
                    break;
            }
            updateTargetSkills(selectedArmor);
            // 4. 해당 부위 필터링 리스트 재평가
            if (selectedArmors[0] == null) filteredHead = filteringHeads(headArmors, slotMin, slotMax);
            if (selectedArmors[1] == null) filteredChest = filteringChests(chestArmors, slotMin, slotMax);
            if (selectedArmors[2] == null) filteredGloves = filteringGloves(glovesArmors, slotMin, slotMax);
            if (selectedArmors[3] == null) filteredWaist = filteringWaists(waistArmors, slotMin, slotMax);
            if (selectedArmors[4] == null) filteredLegs = filteringLegs(legsArmors, slotMin, slotMax);
        }
        // 최종 선택된 장비들 출력
        resultView.setText(""); // TextView 초기화
        for (int i = 0; i < selectedArmors.length; i++) {
            if (selectedArmors[i] != null) {
                resultView.append(selectedArmors[i].getName() + " (" + selectedArmors[i].getType() + ")\n");
            }
        }
    }

    private List<Pair<Charm, Integer>> filteringCharms() {
        // 1. 호석 필터링: 필요한 레벨을 초과하지 않는 호석들만 선택
        List<Pair<Charm, Integer>> filteredCharms = new ArrayList<>();
        // targetSkills는 미리 정의된 필요한 스킬들의 이름 리스트라고 가정
        for (Charm charm : charms) {
            for (Charm.CharmRank charmRank : charm.getRanks()){
                for (Skill.Rank charmSkill : charmRank.getSkills()){
                    if (charmSkill != null){
                        for (Pair<String, Integer> skill : targetSkills){
                            if (skill.first.equals(charmSkill.getSkillName()) && skill.second >= charmSkill.getLevel()){
                                // 이미 존재하는 호석인지 확인
                                boolean exists = false;
                                for (int i = 0; i < filteredCharms.size(); i++) {
                                    Pair<Charm, Integer> existing = filteredCharms.get(i);

                                    if (existing.first.equals(charm)) {
                                        // 이미 있는 호석이면 Skill.Rank를 갱신
                                        if (existing.second < charmSkill.getLevel()) {
                                            filteredCharms.set(i, new Pair<>(charm, charmSkill.getLevel()));
                                        }
                                        exists = true;
                                        break;
                                    }
                                }
                                // 리스트에 없는 경우 새로 추가
                                if (!exists) {
                                    filteredCharms.add(new Pair<>(charm, charmSkill.getLevel()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return filteredCharms;
    }

    private  List<Decoration> filteringDeorations(){
        List<Decoration> filteredDecorations = new ArrayList<>();
        for (Decoration decoration : decorations) {
            boolean isValid = true;
            for (Skill.Rank rank : decoration.getSkills()) {
                boolean isMatched = false;
                for (Pair<String, Integer> skill : targetSkills) {
                    if (rank.getSkillName().equals(skill.first) && rank.getLevel() <= skill.second) {
                        isMatched = true;
                        break;
                    }
                }
                if (!isMatched) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                filteredDecorations.add(decoration);
            }
        }
        return filteredDecorations;
    }

    private List<Pair<Armor, Integer>> filteringHeads(List<Armor> headArmors, int slotMin, int slotMax){
        List<Pair<Armor, Integer>> filteredHead = new ArrayList<>();
        for (Armor armor : headArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax);
            if (score > 3) {
                filteredHead.add(new Pair<> (armor, score));
            }
        }
        filteredHead.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredHead;
    }

    private List<Pair<Armor, Integer>> filteringChests(List<Armor> chestArmors, int slotMin, int slotMax){
        List<Pair<Armor, Integer>> filteredChest = new ArrayList<>();
        for (Armor armor : chestArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax);
            if (score > 3) {
                filteredChest.add(new Pair<> (armor, score));
            }
        }
        filteredChest.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredChest;
    }

    private List<Pair<Armor, Integer>> filteringGloves(List<Armor> glovesArmors, int slotMin, int slotMax){
        List<Pair<Armor, Integer>> filteredGloves = new ArrayList<>();
        for (Armor armor : glovesArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax);
            if (score > 3) {
                filteredGloves.add(new Pair<> (armor, score));
            }
        }
        filteredGloves.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredGloves;
    }

    private List<Pair<Armor, Integer>> filteringWaists(List<Armor> waistArmors, int slotMin, int slotMax){
        List<Pair<Armor, Integer>> filteredWaist = new ArrayList<>();
        for (Armor armor : waistArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax);
            if (score > 3) {
                filteredWaist.add(new Pair<> (armor, score));
            }
        }
        filteredWaist.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredWaist;
    }

    private List<Pair<Armor, Integer>> filteringLegs(List<Armor> legsArmors, int slotMin, int slotMax){
        List<Pair<Armor, Integer>> filteredLegs = new ArrayList<>();
        for (Armor armor : legsArmors){
            int score = 0;
            score += calculateSkillScore(armor);
            score += calculateSlotScore(armor, slotMin, slotMax);
            if (score > 3) {
                filteredLegs.add(new Pair<> (armor, score));
            }
        }
        filteredLegs.sort((pair1, pair2) -> Integer.compare(pair2.second, pair1.second));
        return filteredLegs;
    }

    private Armor chooseArmor(List<Pair<Armor, Integer>> filteredHead, List<Pair<Armor, Integer>> filteredChest, List<Pair<Armor, Integer>> filteredGloves, List<Pair<Armor, Integer>> filteredWaist, List<Pair<Armor, Integer>> filteredLegs, boolean[] checkSelected){
        List<List<Pair<Armor, Integer>>> filteredLists = Arrays.asList(
                filteredHead, filteredChest, filteredGloves, filteredWaist, filteredLegs
        );
        Armor selectedArmor = null;
        int highestScore = Integer.MIN_VALUE;
        int i = 0;
        for (List<Pair<Armor, Integer>> filteredList : filteredLists) {
            if (!checkSelected[i] && !filteredList.isEmpty() && filteredList.get(0).second > highestScore) {
                selectedArmor = filteredList.get(0).first;
            }
            i++;
        }
        return selectedArmor;
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
    private int calculateSlotScore(Armor armor, int slotMin, int slotMax) {
        int slotScore = 0;
        // 4레벨 슬롯은 무조건 +2점
        for (Armor.Slot slot : armor.getSlots()) {
            if (slot.getRank() == 4 && slotMax == 4) {
                slotScore += 2;
            }
            else if (slot.getRank() >= slotMin) {
                slotScore += 1;
            }
        }
        return slotScore;
    }
    //선택된 장비를 받아서 targetSkills 갱신
    private void updateTargetSkills(Armor armor){
        List<Pair<String, Integer>> skillsToRemove = new ArrayList<>();
        for (Skill.Rank armorSkill : armor.getSkills()) {
            for (int i = 0; i < targetSkills.size(); i++) {
                Pair<String, Integer> targetSkill = targetSkills.get(i);
                if (armorSkill.getSkillName().equals(targetSkill.first)) {
                    int updatedLevel = targetSkill.second - armorSkill.getLevel();

                    if (updatedLevel <= 0) {
                        // 해당 targetSkill을 targetSkills에서 제거
                        skillsToRemove.add(targetSkill);
                    } else {
                        // 새 Pair 객체로 값 업데이트
                        targetSkills.set(i, new Pair<>(targetSkill.first, updatedLevel));
                    }
                    break;
                }
            }
        }
        targetSkills.removeAll(skillsToRemove);
    }
    // Weapon 슬롯 값을 정수 배열로 파싱
    private int[] parseSlotValues(String slotText) {
        if ("없음".equals(slotText)) {
            return new int[]{0, 0, 0};
        }
        String[] parts = slotText.split(", ");
        int[] slots = new int[3];
        for (int i = 0; i < parts.length; i++) {
            slots[i] = Integer.parseInt(parts[i]);
        }
        return slots;
    }

    private List<String> createSkillOptions(int maxLevel) {
        List<String> options = new ArrayList<>();
        options.add("없음");
        for (int i = 1; i <= maxLevel; i++) {
            options.add("Level " + i);
        }
        return options;
    }
}