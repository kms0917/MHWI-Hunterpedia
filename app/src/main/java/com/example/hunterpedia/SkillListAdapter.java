package com.example.hunterpedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class SkillListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupList;
    private Map<String, List<SelectedSkill>> groupSkillsMap;
    private LayoutInflater inflater;
    private OnSkillSelectedListener listener;

    public SkillListAdapter(Context context, List<String> groupList, Map<String, List<SelectedSkill>> groupSkillsMap, OnSkillSelectedListener listener) {
        this.context = context;
        this.groupList = groupList;
        this.groupSkillsMap = groupSkillsMap;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupSkillsMap.get(groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupSkillsMap.get(groupList.get(groupPosition)).get(childPosition);
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
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }

        TextView groupName = convertView.findViewById(android.R.id.text1);
        groupName.setText(groupList.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // ViewHolder 패턴을 사용하여 convertView의 성능을 최적화
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_skill_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.skillName = convertView.findViewById(R.id.skill_name);
            viewHolder.skillLevelSpinner = convertView.findViewById(R.id.skill_level_spinner);
            convertView.setTag(viewHolder); // ViewHolder 저장
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); // 이미 생성된 ViewHolder 사용
        }

        // 자식 항목의 이름을 설정 (이 부분은 필요 시에만 업데이트)
        SelectedSkill skill = groupSkillsMap.get(groupList.get(groupPosition)).get(childPosition);

        // 처음 설정된 skill name만 변경하고, 이후에는 변경되지 않도록
        if (viewHolder.skillName.getText().toString().isEmpty()) {
            viewHolder.skillName.setText(skill.getName());
        }

        // 스피너 설정 (이 부분도 스피너의 선택 상태만 업데이트)
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, skill.getSpinnerOptions());
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (viewHolder.skillLevelSpinner.getAdapter() == null) {
            viewHolder.skillLevelSpinner.setAdapter(levelAdapter);
        }

        // 이미 선택된 레벨이 있으면 그 값을 설정 (이 부분은 이미 설정된 상태를 유지)
        if (viewHolder.skillLevelSpinner.getSelectedItemPosition() != skill.getSelectedLevel()) {
            viewHolder.skillLevelSpinner.setSelection(skill.getSelectedLevel());
        }

        // 스피너에서 선택된 레벨을 targetSkills에 추가하는 로직
        viewHolder.skillLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    int selectedLevel = position;
                    skill.setSelectedLevel(selectedLevel);  // 선택된 레벨 저장
                    if (listener != null) {
                        listener.onSkillSelected(skill, selectedLevel);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return convertView;
    }

    // ViewHolder 클래스: 뷰 홀더 패턴을 사용하여 뷰 항목을 캐시
    static class ViewHolder {
        TextView skillName;
        Spinner skillLevelSpinner;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
