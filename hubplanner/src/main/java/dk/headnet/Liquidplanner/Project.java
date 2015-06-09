package dk.headnet.Liquidplanner;

public class Project {
	private String description;
	private String name;
	private String id;
	private String type;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "LPProject [description=" + description + ", name=" + name
				+ ", id=" + id + ", type=" + type + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Project other = (Project) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	

}


/*

{
"work": 94.4,
"alerts": [],
"client_id": 2200408,
"client_name": "Headnet - internt",
"created_at": "2011-06-29T20:14:55+00:00",
"created_by": 137697,
"custom_field_values": {
    "Customer Report Type": "None"
},
"done_on": null,
"started_on": "2011-10-23",
"delay_until": null,
"parent_delay_until": null,
"description": "Just a label! Please don't put things in this project",
"earliest_finish": "2015-06-02T13:50:00+00:00",
"earliest_start": "2015-06-02T13:50:00+00:00",
"expected_finish": "2015-06-02T13:50:00+00:00",
"expected_start": "2015-06-02T13:50:00+00:00",
"p98_finish": "2015-06-02T13:50:00+00:00",
"global_priority": [
    1,
    3
],
"has_note": false,
"high_effort_remaining": 0,
"is_done": false,
"is_on_hold": false,
"effective_is_on_hold": false,
"latest_finish": "2015-06-02T13:50:00+00:00",
"low_effort_remaining": 0,
"manual_alert": "",
"max_effort": null,
"name": "___ HEADNET GENERAL  __________________________________",
"assignments": [
    {
        "person_id": 0,
        "team_id": null,
        "low_effort_remaining": 0,
        "high_effort_remaining": 0,
        "treeitem_id": 3149372,
        "space_id": 35031,
        "activity_id": null,
        "is_done": false,
        "daily_limit": null,
        "position": 0,
        "hours_logged": 0,
        "expected_finish": null,
        "expected_start": null,
        "can_destroy": false,
        "type": "Assignment",
        "id": 2563913
    }
],
"parent_id": 1400534,
"parent_ids": [
    1400534
],
"parent_crumbs": [],
"promise_by": null,
"parent_promise_by": null,
"external_reference": "",
"updated_at": "2012-09-21T11:20:57+00:00",
"updated_by": 137697,
"type": "Project",
"id": 3149372
},

*/