package cdl.android.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import cdl.android.general.ServerResponse;
import cdl.android.ui.challenge.ActiveChallenge;
import cdl.android.ui.challenge.ChallengeInfo;
import cdl.android.ui.challenge.menu.RChallengeList;

public class ChallengeHandler {
	private static HttpClient mHttpClient = new DefaultHttpClient();

	private ChallengeHandler() {

	}

	/**
	 * Sends a challenge to the server using the data received as parameter
	 * 
	 * @param challenge_id
	 *            The challenge id to send to
	 * @param data
	 *            The data to send.
	 */
	public static void sendPost(Context context, int challenge_id, List<NameValuePair> data) {
		//TODO: check errors
		ApiHandler.sendPost(ApiHandler.baseChallengeURL + challenge_id, data, context);
	}

	/**
	 * Posts data on the server TODO move this to a different thread, it can
	 * take a lot of time!
	 * 
	 * @param data
	 *            content for post
	 */
	public static void post(Context context, ActiveChallenge data) {

		/** Build Post */
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		Map<String, String> wrapped = data.getWrappable();
		for (String keySet : wrapped.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(keySet, wrapped.get(keySet)));
		}
		sendPost(context, data.getChallengeId(), nameValuePairs);
	}

	/**
	 * Change the challenge state of a player's challenge.
	 * 
	 * @param challenge_id
	 *            The challenge's id.
	 * @param otherState
	 *            The state, 0 to accept, 2 to refuse!
	 */
	public static void changeChallengeState(Context context, int challenge_id, int otherState) {
		if (otherState == 0) {
			ServerResponse rsp = ApiHandler.get(ApiHandler.baseChallengeURL 
					+ challenge_id + "/accept/", context);
		}
		if (otherState == 2) {
			ServerResponse rsp = ApiHandler.get(ApiHandler.baseChallengeURL 
					+ challenge_id + "/refuse/", context);
		}
	}

	/**
	 * Gets information about a specific challenge.
	 * 
	 * @param challenge_id
	 *            The challenge's id.
	 * @return The parsed Challenge Info.
	 */
	public static ChallengeInfo getChallengeInfo(Context context, int challenge_id) {
		//TODO: check errors
		ServerResponse rsp = ApiHandler.get(ApiHandler.baseChallengeURL 
				+ challenge_id + "/", context);
		return new ChallengeInfo(rsp.getData());
	}

	/**
	 * Start a challenge with another player.
	 * 
	 * @param otherPlayer
	 *            The other player's username.
	 * @return whether the challenge was successfully started.
	 */
	public static boolean startChallenge(Context context, String otherPlayer) {
		//TODO: check errors
		ServerResponse rsp = ApiHandler.get(ApiHandler.challengeLaunchURL 
				+ otherPlayer + "/", context);
		return rsp.getData() != null;
	}
	
	/**
	 * Gets the challenge list for the logged user.
	 * 
	 * @return A wrapper class describing all the challenges.
	 */
	public static RChallengeList getChallengeList(Context context) {
		//TODO: check errors
		ServerResponse rsp = ApiHandler.getArray(ApiHandler.challengeListURL, context);
		return new RChallengeList(rsp.getArrayData());
	}

}
