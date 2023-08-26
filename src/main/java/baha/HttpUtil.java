package main.java.baha;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


public class HttpUtil {
	public static final Map<String,String> newSongsMap = new ConcurrentHashMap<String, String>();
	public static List<List<String>> getContentFor(String val) throws IOException, InterruptedException {

		if(null== val || val.length()==0)
			val = "all";
		URL url = new URL(
				BahaStater.properties.getProperty(Constants.GOOGLE_SHEET_URL)+"?value="+val);
		//https://script.google.com/macros/s/AKfycbzXOkKS80I1xjW8FjvwCYVlV184N3H3rTrAYfOjcLjv_QqkCPaiRlD-8EJZKTc8GYYBTQ/exec
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
		Gson gson = new Gson();
		Type listType = new TypeToken<List<List<String>>>() {}.getType();
		List<List<String>> result = gson.fromJson(content.toString(), listType);
		
		return result;

	}
	
	public static BibleResponseDTO getOnlineVerseForTranslation(String translation, String book, String bookId, String chapter, String verse) throws IOException, InterruptedException {

		if(null== translation || translation.length()==0)
			return null;
		Gson gson = new Gson();
		BibleRequestDTO requestJson = new BibleRequestDTO();
		requestJson.setBook(book);
		requestJson.setBid(bookId);
		requestJson.setChapter(chapter);
		requestJson.setVerse(verse);
		requestJson.setVersion(translation);
		requestJson.setFound(1);
		requestJson.setChapter_roman("");
		requestJson.setNext_chapter("");
		String requestJsonString = gson.toJson(requestJson);
		//String requestJson=book+(String.format("%03d", Integer.valueOf(chapter)) + String.format("%03d", Integer.valueOf(verse)));
		long startTime = System.currentTimeMillis();
		//URL url = new URL("https://bible-go-api.rkeplin.com/v1/books/"+book+"/chapters/"+chapter+"/"+prepareId+"?translation="+translation);
		//URL url = new URL("https://jsonbible.com/search/verses.php?json={ \"book\": \"John\", \"bid\": \"43\", \"chapter\": \"3\", \"chapter_roman\": \"\", \"verse\": \"16\", \"found\": 1, \"next_chapter\": \"\", \"version\": \"kjv\" }");
		URL url = new URL("https://jsonbible.com/search/verses.php?json="+requestJsonString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
		System.out.println("Verse api took "+(System.currentTimeMillis()-startTime) + "ms");
		Type listType = new TypeToken<BibleResponseDTO>() {}.getType();
		BibleResponseDTO result = gson.fromJson(content.toString(), listType);
		//System.out.println(result);
		return result;

	}
	
	public static List<BibleResponseDTO> getOnlineVerseByChapterForTranslation(String translation, String book, String chapter) throws IOException, InterruptedException {

		if(null== translation || translation.length()==0)
			return null;
		
		long startTime = System.currentTimeMillis();
		//URL url = new URL("https://bible-go-api.rkeplin.com/v1/books/"+book+"/chapters/"+chapter+"?translation="+translation);
		
		URL url = new URL("https://jsonbible.com/search/verses.php?json={ \"book\": \"John\", \"bid\": \"43\", \"chapter\": \"3\", \"chapter_roman\": \"\", \"verse\": \"16\", \"found\": 1, \"next_chapter\": \"\", \"version\": \"kjv\" }");
		System.out.println(url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
		//System.out.println("Verse api took "+(System.currentTimeMillis()-startTime) + "ms");
		Gson gson = new Gson();
		//Type listType = new TypeToken<List<BibleResponseDTO>>() {}.getType();
		//List<BibleResponseDTO> result = gson.fromJson(content.toString(), listType);
//		System.out.println(content.toString());
		return null;//result;

	}
	
	
	
	public static void syncGoogle() throws IOException, InterruptedException {
		
		URL url = new URL(BahaStater.properties.getProperty(Constants.GOOGLE_SHEET_URL)+"?value=all");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
		Gson gson = new Gson();
		Type listType = new TypeToken<List<List<String>>>() {}.getType();
		List<List<String>> result = gson.fromJson(content.toString(), listType);
		Map<String, String> songMap = new ConcurrentHashMap<String, String>();
		
		if(CollectionUtils.isNotEmpty(result)) {
			result.parallelStream().forEach(sublist -> {
				String contents = sublist.get(1);
				if(contents.contains("\n\n") || contents.contains("\r\n\r\n")) {
				} else {
					contents = contents.replaceAll("\r\n", "\n")
							.replaceAll("\n ", "\n")
							.replaceAll("\n\n\n\n", "\n=\n")
							.replaceAll("\n\n==\n\n", "\n==\n")
							.replaceAll("\n\n", "\n")
							.replaceAll("\n=\n", "\n\n")
							.replaceAll("\n", "\r\n");
					
				}
				
				
				if(BAHAMenu.songsMap.keySet().parallelStream().noneMatch(a -> Objects.equals( a.trim().toLowerCase(),sublist.get(0).trim().toLowerCase()))) {
					newSongsMap.put(sublist.get(0), contents);
				}
//				songMap.put(sublist.get(0), sublist.get(1).replaceAll("\r\n", "\n").replaceAll("\n[ ]{2,}\n", "\n~\n")
//						.replaceAll("[ ]{2,}", "").replaceAll("[\n \n]{2,}", "\n").replaceAll("[\n]{2,}", "\n\n").replaceAll("\n~\n", "\n \n")
//						.replaceAll("([\\w+,.-])\n(\\W+)", "\n==\n$2").replaceAll("\n", "\r\n"));
				songMap.put(sublist.get(0), contents);
			});
		}
		
		if(MapUtils.isNotEmpty(songMap)) {
			BAHAMenu.songsMap.putAll(songMap);
		}
		System.out.println("Gsync completed");
		
		if(MapUtils.isNotEmpty(newSongsMap)) {
			updateLocalFileForNewSong(newSongsMap);
		}
		JOptionPane.showMessageDialog(BahaStater.initFrame, "Google Sync completed successfully!");
		WorshipPannel.fcb.refresh(new ArrayList<String>(BAHAMenu.songsMap.keySet()));
	}
	
	public static void postContent(Map<String, String> rowData, String action) throws IOException, InterruptedException {

		try {
			if(null == rowData || MapUtils.isEmpty(rowData))
				return;
			URL url = new URL(BahaStater.properties.getProperty(Constants.GOOGLE_SHEET_URL));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Content-Type", "text/plain");
			con.setRequestProperty("Accept", "text/plain");
			Gson gson = new Gson();
			List<List<String>> postData=new ArrayList<List<String>>();
			for(Map.Entry<String,String> entry:rowData.entrySet()) {
				List<String> entryData = new ArrayList<String>(Arrays.asList(entry.getKey().trim(),entry.getValue().replaceAll("[ ]{2,}", " ")));
				postData.add(entryData);
			}
			
			final String POST_PARAMS = "{\"action\":\""+action+"\",\"body\":"+gson.toJson(postData)+"}";
			
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(POST_PARAMS.getBytes());
			os.flush();
			os.close();
			
			
			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// print result
				System.out.println(response.toString());
				JOptionPane.showMessageDialog(BahaStater.initFrame, response+"!");
			} else {
				System.out.println("POST request not worked "+responseCode);
				System.out.println("Conn Response: "+con.getResponseMessage());
				JOptionPane.showMessageDialog(BahaStater.initFrame, responseCode+": "+con.getResponseMessage()+"!");
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

//	public static void main(String[] args) {
//		HttpUtil util = new HttpUtil();
//		try {
//			util.getContentFor("Vijeta");
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}
	
	
	public static void updateLocalFileForNewSong(Map<String,String> data) throws IOException {

		XSSFSheet sheet = null;
		File fl = new File(Constants.RESOURCES_PATH +  "WorshipSongs.xlsx");
		FileInputStream inp= new FileInputStream(fl);
		XSSFWorkbook workbook = new XSSFWorkbook(inp);
		sheet = workbook.getSheetAt(0);

	      //Create row object
	      XSSFRow row;

	      //Iterate over data and write to sheet
	      Set<String > keyid = data.keySet();
	      int rowid = sheet.getLastRowNum();
	      for (String key : keyid) {
	         row = sheet.createRow(++rowid);
	        
	         String objectArr = data.get(key);
	         Cell cell = row.createCell(0);
	         cell.setCellValue(key.toString());
	         Cell cell1 = row.createCell(1);
	         cell1.setCellValue(objectArr.toString());
	         Cell cell4 = row.createCell(4);
	         cell4.setCellValue(new Date());
	      }
	      //Write the workbook in file system
		try(FileOutputStream out = new FileOutputStream(
				  fl)) {
			 workbook.write(out);
			 workbook.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	      System.out.println("WorshipSongs.xlsx written successfully");
		
	}
	
	
	public static boolean checkInternetConnection () {
		boolean up = false;
		try {
			Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
			int x = process.waitFor();
			if (x == 0) {
			    System.out.println("Connection Successful, "
			                       + "Output was " + x);
			    up = true;
			}
			else {
			    System.out.println("Internet Not Connected, "
			                       + "Output was " + x);
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
		return up;
	}
	
//public static void loadGoogleSlides() throws IOException, InterruptedException {
//		System.out.println(BahaStater.properties.getProperty(Constants.GOOGLE_SLIDE_URL));
//		
//			URL url = new URL(BahaStater.properties.getProperty(Constants.GOOGLE_SLIDE_URL));
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			con.setRequestMethod("GET");
//			InputStream in = con.getInputStream();
//		try(in) {
//			System.out.println(in.toString());
//			Gson gson = new Gson();
//			Type listType = new TypeToken<List<List<Byte>>>() {}.getType();
//			System.out.println(">>>"+gson.toJson(in.readAllBytes()));
//			String result = gson.fromJson(in.toString(), listType);
//			System.out.println(result);
//			result.forEach(bytes -> {
//				FileOutputStream out;
//				byte[] byts;
//				try {
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
//					ObjectOutputStream oos;
//					oos = new ObjectOutputStream(baos);
//					oos.writeObject(bytes);
//					
//					byts = baos.toByteArray();
//					
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				
////				BufferedImage img = ImageIO.read(new File(b));
//			});
//			String json = gson.toJson(in.readAllBytes());
//			System.out.println(json);
//			System.out.println(gson.);
//			Type listType = new TypeToken<List<byte[]>>() {}.getType();
//			List<byte[]> result = gson.fromJson(be, listType);
//			System.out.println(result);
//		} catch (JsonSyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		JOptionPane.showMessageDialog(BahaStater.initFrame, "Google Slide loaded successfully!");
//	}
}
