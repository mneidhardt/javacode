package dk.headnet.Liquidplanner;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
	private String activity_id;
	private String project_id;
	private String client_id;
	private String client_name;
	private String start_date;
	private String finish_date;
	private Assignment[] assignments;
	
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getFinish_date() {
		return finish_date;
	}
	public void setFinish_date(String finish_date) {
		this.finish_date = finish_date;
	}
	public Assignment[] getAssignments() {
		return assignments;
	}
	public void setAssignments(Assignment[] assignments) {
		this.assignments = assignments;
	}
	@Override
	public String toString() {
		return "Activity [activity_id=" + activity_id + ", project_id="
				+ project_id + ", client_id=" + client_id + ", client_name="
				+ client_name + ", start_date=" + start_date + ", finish_date="
				+ finish_date + ", assignments=" + Arrays.toString(assignments)
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activity_id == null) ? 0 : activity_id.hashCode());
		result = prime * result + Arrays.hashCode(assignments);
		result = prime * result
				+ ((client_id == null) ? 0 : client_id.hashCode());
		result = prime * result
				+ ((client_name == null) ? 0 : client_name.hashCode());
		result = prime * result
				+ ((finish_date == null) ? 0 : finish_date.hashCode());
		result = prime * result
				+ ((project_id == null) ? 0 : project_id.hashCode());
		result = prime * result
				+ ((start_date == null) ? 0 : start_date.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		if (activity_id == null) {
			if (other.activity_id != null)
				return false;
		} else if (!activity_id.equals(other.activity_id))
			return false;
		if (!Arrays.equals(assignments, other.assignments))
			return false;
		if (client_id == null) {
			if (other.client_id != null)
				return false;
		} else if (!client_id.equals(other.client_id))
			return false;
		if (client_name == null) {
			if (other.client_name != null)
				return false;
		} else if (!client_name.equals(other.client_name))
			return false;
		if (finish_date == null) {
			if (other.finish_date != null)
				return false;
		} else if (!finish_date.equals(other.finish_date))
			return false;
		if (project_id == null) {
			if (other.project_id != null)
				return false;
		} else if (!project_id.equals(other.project_id))
			return false;
		if (start_date == null) {
			if (other.start_date != null)
				return false;
		} else if (!start_date.equals(other.start_date))
			return false;
		return true;
	}

	
}

/*

{
    "activity_id": 60889,
    "work": 75,
    "alerts": [],
    "project_id": 3072504,
    "client_id": 2200408,
    "client_name": "Headnet - internt",
    "created_at": "2014-07-09T09:07:51+00:00",
    "created_by": 129079,
    "custom_field_values": {
        "Customer Report Type": "None"
    },
    "done_on": "2014-09-07",
    "started_on": "2014-08-25",
    "start_date": "2014-08-25",
    "description": "",
    "earliest_finish": null,
    "earliest_start": null,
    "expected_finish": null,
    "expected_start": null,
    "p98_finish": null,
    "global_priority": [
        1,
        101,
        402,
        1043,
        101
    ],
    "global_package_priority": null,
    "has_note": false,
    "high_effort_remaining": 0,
    "is_done": true,
    "is_on_hold": false,
    "effective_is_on_hold": false,
    "is_packaged_version": false,
    "is_shared": false,
    "latest_finish": null,
    "low_effort_remaining": 0,
    "manual_alert": "",
    "max_effort": null,
    "name": "Sommerferie del 2",
    "assignments": [
        {
            "person_id": 418815,
            "team_id": null,
            "low_effort_remaining": 0,
            "high_effort_remaining": 0,
            "treeitem_id": 16008861,
            "space_id": 35031,
            "activity_id": 60889,
            "is_done": true,
            "daily_limit": null,
            "position": 0,
            "hours_logged": 75,
            "expected_finish": null,
            "expected_start": null,
            "can_destroy": false,
            "type": "Assignment",
            "id": 356703
        }
    ],
    "parent_id": 15590393,
        "finish_date": "2014-09-07",
        "external_reference": null,
        "package_id": null,
        "parent_ids": [
            1400534,
            3072504,
            3072565,
            15590393
        ],
        "parent_crumbs": [
            "Headnet Absence [z]",
            "Vacation (Activity type: 020 Vacation, Holliday, Public holidays)",
            "Michael Neidhardt"
        ],
        "package_ids": [],
        "package_crumbs": [],
        "occurrences": [],
        "updated_at": "2014-09-26T06:53:31+00:00",
        "updated_by": 129079,
        "type": "Event",
        "id": 16008861
    },

*/