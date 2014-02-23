import java.util.*;

/**
 * Uses String.Split() to split the data according to the formatting defined by the protocol. Each method being linked
 * to a particular formatting.
 *
 * @author aquaj
 *
 */
public class Splitter {

	public static final String sep = "##";

	private Splitter(){}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Username -> key "Name"<br />
	 * - Hostname/IP -> key "Host".
	 */
	public static Hashtable<String, String> connect(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("Name", data.split(sep)[0]);
		table.put("Host", data.split(sep)[1]);
		return table;
	}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Username -> key "Name"<br />
	 * - Status content -> key "Status"
	 * - Date -> key "Date"
	 */
	public static Hashtable<String, String> status(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("Name", data.split(sep)[0]);
		table.put("Date", data.split(sep)[1]);
		table.put("Status", data.split(sep)[2]);
		return table;
	}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Username of the poster of commented status -> key "StatusName"<br />
	 * - Status date of publication -> key "StatusDate"<br />
	 * - Username of comment poster -> key "CommentName"<br />
	 * - Content of the comment -> key "Comment"
	 */
	public static Hashtable<String, String> comment(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("StatusName", data.split(sep)[0]);
		table.put("StatusDate", data.split(sep)[1]);
		table.put("CommentName", data.split(sep)[2]);
		table.put("Comment", data.split(sep)[1]);
		return table;
	}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Username of the user requesting friendship -> key "NewFriendName"<br />
	 * - Hostname/IP of the user requesting friendship -> key "NewFriendHost"<br />
	 * - Username of the first friend in the friendlist -> key "Friend1Name"<br />
	 * - Hostname/IP of the first friend in the friendlist -> key "Friend1Host"<br />
	 * - Username of the second friend in the friendlist -> key "Friend2Name"<br />
	 * - Hostname/IP of the second friend in the friendlist-> key "Friend2Host"<br />
	 * ...<br />
	 * - Username of the n-th friend in the friendlist -> key "FriendNName"<br />
	 * - Hostname/IP of the n-th friend in the friendlist -> key "FriendNHost"<br />
	 */
	public static Hashtable<String, String> friendRequest(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("NewFriendName", data.split("##")[0]);
		table.put("NewFriendHost", data.split("##")[1]);
		for(int i=1; i<(data.split("##").length/2); i++)
		{
			table.put("Friend"+i+"Name", data.split("##")[2*i]);
			table.put("Friend"+i+"Host", data.split("##")[2*i+1]);
		}
		return table;
	}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Username -> key "Name"<br />
	 */
	public static Hashtable<String, String> friendResponse(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("Name",data);
		return table;
	}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Username -> key "Name"<br />
	 * - Image -> key "Picture"
	 */
	public static Hashtable<String, String> image(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("Name", data.split(sep)[0]);
		table.put("Picture", data.split(sep)[1]);
		return table;
	}

	/**
	 * @param data - Formatted String containing all the data to be extracted.
	 * @return Hashtable<String, String> containing the data.<br />
	 * - Error message -> key "Error"<br />
	 */
	public static Hashtable<String, String> errorMessage(String data)
	{
		Hashtable<String, String> table = new Hashtable<String, String>();
		return table;
	}
}