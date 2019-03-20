package mx.iteso.miiteso.conectividadWS;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.iteso.miiteso.miiteso.model.ItemMainNotice;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

import static android.content.ContentValues.TAG;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
public class RssServices extends AsyncTask<Void, Void, Boolean> {
    private ArrayList<ItemMainNotice> mFeedModelList = new ArrayList<ItemMainNotice>();
    Context context;
    List<String> urls;
    int cant;
    onTaskCompletedRss taskCompletedRss;


    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";

    //private ProgressDialog dialog;

    public RssServices(Context context, onTaskCompletedRss taskCompletedRss, List<String> urls, int cant) {
        this.context = context;
        this.cant = cant;
        this.urls = urls;
        this.taskCompletedRss = taskCompletedRss;
      //  dialog = new ProgressDialog(context);

    }

    @Override
    protected void onPreExecute() {
       // dialog.setMessage("Cargando servicios rss. Por favor espere.");
        //dialog.show();

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        int cont =0;
        if (urls.size() <= 0)
            return false;
        for (String urlLink : urls) {


                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                /*URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();

                mFeedModelList.addAll(parseFeed(inputStream));
                */

                mFeedModelList.addAll(getRSSFeedItems(urlLink));
                cont++;
                if (cont == urls.size())
                    return true;


        }
        return false;

    }

    @Override
    protected void onPostExecute(Boolean success) {
        //mSwipeLayout.setRefreshing(false);

        if (success) {
            if (taskCompletedRss != null)
                taskCompletedRss.onTaskCompleted(mFeedModelList);
            else
                new Metodos(context).MensajeAUsuario(Constantes.MSG_WEB_SERVICES_ERROR);
            // this.itemMainNotices.addAll(mFeedModelList);
        } else {
            Toast.makeText(context,
                    "Error al obtener el Rss",
                    Toast.LENGTH_SHORT).show();
            if (!mFeedModelList.isEmpty())
                taskCompletedRss.onTaskCompleted(mFeedModelList);

        }
        //if (dialog.isShowing()) {
        //    dialog.dismiss();
        //}
    }




    public List<ItemMainNotice> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String startDate = null;
        String endDate = null;
        String[] starHour = new String[]{};
        String[] endHour = new String[]{};

        boolean isItem = false;
        List<ItemMainNotice> items = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream,  null);
            xmlPullParser.nextTag();


            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("pubDate")) {
                    String Date = dateFormat.format(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(result));
                    startDate = Date;
                    endDate = Date;

                    starHour = Date.split(" ");
                    endHour = Date.split(" ");

                }

                if (title != null && link != null && startDate != null && endDate != null) {
                    if (isItem) {
                        ItemMainNotice item = new ItemMainNotice(title, startDate, endDate, link, 6, starHour[1].substring(0,5), endHour[1].substring(0,5));
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    startDate = null;
                    endDate = null;
                    starHour = null;
                    endHour = null;
                    isItem = false;
                }
            }

            return items;
        } catch (ParseException e) {
            e.printStackTrace();
            return items;
        } finally {
            inputStream.close();
        }
    }


    public List<ItemMainNotice> getRSSFeedItems(String rss_url) {
        List<ItemMainNotice> itemsList = new ArrayList<ItemMainNotice>();
        String rss_feed_xml;
        String[] starHour = new String[]{};
        String[] endHour = new String[]{};

        rss_feed_xml = this.getXmlFromUrl(rss_url);
        if (rss_feed_xml != null) {
            try {
                Document doc = this.getDomElement(rss_feed_xml);
                NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                Element e = (Element) nodeList.item(0);

                NodeList items = e.getElementsByTagName(TAG_ITEM);
                for (int i = 0; i < items.getLength(); i++) {
                    Element e1 = (Element) items.item(i);

                    String title = this.getValue(e1, TAG_TITLE);
                    String link = this.getValue(e1, TAG_LINK);
                    String description = this.getValue(e1, TAG_DESRIPTION);
                    String pubdate = this.getValue(e1, TAG_PUB_DATE);
                    String guid = this.getValue(e1, TAG_GUID);

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String date = dateFormat.format(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(pubdate));
                    starHour = date.split(" ");
                    endHour = date.split(" ");
                    ItemMainNotice rssItem = new ItemMainNotice(title,date,date, link, 6,starHour[1].substring(0,5), endHour[1].substring(0,5));

                    // adding item to list
                    itemsList.add(rssItem);
                }
            } catch (Exception e) {
                // Check log for errors
                e.printStackTrace();
            }
        }

        // return item list
        return itemsList;
    }


    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }


    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || (child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }


    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

}
