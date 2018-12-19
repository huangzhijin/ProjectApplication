//package com.click.cn.view.contact.other;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.click.cn.R;
//import com.click.cn.view.contact.Contact;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ContactsActivity extends AppCompatActivity {
//
//    RecyclerView mListView;
//    EditText etSearch;
//    ImageView ivClearText;
//
//    private SideBar sideBar;
//    private TextView dialog;
//
//    private List<Contact> mAllContactsList;
//    private ContactsSortAdapter adapter;
//    Context mContext;
//    /**
//     * 汉字转换成拼音的类
//     */
//    private CharacterParser characterParser;
//
//    /**
//     * 根据拼音来排列ListView里面的数据类
//     */
//    private PinyinComparator pinyinComparator;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_contacts);
//        mContext = getApplicationContext();
//        init();
//    }
//
//    private void init() {
//        initView();
//        initListener();
//        loadContacts();
//    }
//
//    private void initListener() {
//
//        /** 清除输入字符 **/
//        ivClearText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                etSearch.setText("");
//            }
//        });
//        etSearch.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable e) {
//
//                String content = etSearch.getText().toString();
//                if ("".equals(content)) {
//                    ivClearText.setVisibility(View.INVISIBLE);
//                } else {
//                    ivClearText.setVisibility(View.VISIBLE);
//                }
//                if (content.length() > 0) {
//                    ArrayList<Contact> fileterList = (ArrayList<Contact>) search(content);
//                    adapter.updateListView(fileterList);
//                    // mAdapter.updateData(mContacts);
//                } else {
//                    adapter.updateListView(mAllContactsList);
//                }
//                mListView.setSelection(0);
//
//            }
//
//        });
//
//        // 设置右侧[A-Z]快速导航栏触摸监听
//        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//
//            @Override
//            public void onTouchingLetterChanged(String s) {
//                // 该字母首次出现的位置
//                int position = adapter.getPositionForSection(s.charAt(0));
//                if (position != -1) {
//                    mListView.setSelection(position);
//                }
//            }
//        });
//        // item事件
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
//                ViewHolder viewHolder = (ViewHolder) view.getTag();
//                viewHolder.cbChecked.performClick();
//                adapter.toggleChecked(position);
//            }
//        });
//
//    }
//
//    private void initView() {
//        sideBar = (SideBar) findViewById(R.id.sidrbar);
//        dialog = (TextView) findViewById(R.id.dialog);
//        sideBar.setTextView(dialog);
//        ivClearText = (ImageView) findViewById(R.id.ivClearText);
//        etSearch = (EditText) findViewById(R.id.et_search);
//        mListView = (RecyclerView) findViewById(R.id.lv_contacts);
//
//        /** 给ListView设置adapter **/
//        characterParser = CharacterParser.getInstance();
//        mAllContactsList = new ArrayList<Contact>();
//        pinyinComparator = new PinyinComparator();
//        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
//        adapter = new ContactsSortAdapter(this, mAllContactsList);
//        mListView.setAdapter(adapter);
//    }
//
//    /**
//     * 加载联系人数据
//     */
//    private void loadContacts() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 插叙
//                    String queryTye[] = { Phone.DISPLAY_NAME, Phone.NUMBER, "sort_key", "phonebook_label",
//                            Phone.PHOTO_ID };
//                    ContentResolver resolver = getApplicationContext().getContentResolver();
//                    Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, queryTye, null, null,
//                            "sort_key COLLATE LOCALIZED ASC");
//                    if (phoneCursor == null || phoneCursor.getCount() == 0) {
//                        Toast.makeText(getApplicationContext(), "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(Phone.NUMBER);
//                    int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME);
//                    int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");
//                    int PHONEBOOK_LABEL = phoneCursor.getColumnIndex("phonebook_label");
//                    int PHOTO_ID = phoneCursor.getColumnIndex(Phone.PHOTO_ID);
//                    if (phoneCursor.getCount() > 0) {
//                        mAllContactsList = new ArrayList<Contact>();
//                        while (phoneCursor.moveToNext()) {
//                            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
//                            if (TextUtils.isEmpty(phoneNumber))
//                                continue;
//                            // 头像id
//                            long photoId = phoneCursor.getLong(PHOTO_ID);
//                            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
//                            String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
//                            String book = phoneCursor.getString(PHONEBOOK_LABEL);
//                            Contact Contact = new Contact(contactName, phoneNumber, sortKey);
//                            // //优先使用系统sortkey取,取不到再使用工具取
//                            // String sortLetters =
//                            // getSortLetterBySortKey(sortKey);
//                            // Log.i("main", "sortLetters:"+sortLetters);
//                            // if (sortLetters == null) {
//                            // sortLetters = getSortLetter(contactName);
//                            // }
//                            if (book == null) {
//                                book = "#";
//                            } else if (book.equals("#")) {
//                                book = "#";
//                            } else if (book.equals("")) {
//                                book = "#";
//                            }
//                            Contact.sortLetters = book;
//                            Contact.sortToken = parseSortKey(book);
//                            mAllContactsList.add(Contact);
//                        }
//                    }
//                    phoneCursor.close();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            Collections.sort(mAllContactsList, pinyinComparator);
//                            adapter.updateListView(mAllContactsList);
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.e("xbc", e.getLocalizedMessage());
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 名字转拼音,取首字母
//     *
//     * @param name
//     * @return
//     */
//    private String getSortLetter(String name) {
//        String letter = "#";
//        if (name == null) {
//            return letter;
//        }
//        // 汉字转换成拼音
//        // String pinyin = characterParser.getSelling(name);
//        String pinyin = PinYin.getPinYin(name);
//        Log.i("main", "pinyin:" + pinyin);
//        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);
//
//        // 正则表达式，判断首字母是否是英文字母
//        if (sortString.matches("[A-Z]")) {
//            letter = sortString.toUpperCase(Locale.CHINESE);
//        }
//        return letter;
//    }
//
//    /**
//     * 取sort_key的首字母
//     *
//     * @param sortKey
//     * @return
//     */
//    private String getSortLetterBySortKey(String sortKey) {
//        if (sortKey == null || "".equals(sortKey.trim())) {
//            return null;
//        }
//        String letter = "#";
//        // 汉字转换成拼音
//        String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
//        // 正则表达式，判断首字母是否是英文字母
//        if (sortString.matches("[A-Z]")) {
//            letter = sortString.toUpperCase(Locale.CHINESE);
//        }
//        return letter;
//    }
//
//    /**
//     * 模糊查询
//     *
//     * @param str
//     * @return
//     */
//    private List<Contact> search(String str) {
//        List<Contact> filterList = new ArrayList<Contact>();// 过滤后的list
//        // if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
//        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式
//            // 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
//            String simpleStr = str.replaceAll("\\-|\\s", "");
//            for (Contact contact : mAllContactsList) {
//                if (contact.number != null && contact.name != null) {
//                    if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
//                        if (!filterList.contains(contact)) {
//                            filterList.add(contact);
//                        }
//                    }
//                }
//            }
//        } else {
//            for (Contact contact : mAllContactsList) {
//                if (contact.number != null && contact.name != null) {
//                    // 姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
//                    if (contact.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
//                            || contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
//                            .contains(str.toLowerCase(Locale.CHINESE))
//                            || contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE))
//                            || contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
//                            .contains(str.toLowerCase(Locale.CHINESE))) {
//                        if (!filterList.contains(contact)) {
//                            filterList.add(contact);
//                        }
//                    }
//                }
//            }
//        }
//        return filterList;
//    }
//
//    String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配
//
//    // String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配
//    /**
//     * 解析sort_key,封装简拼,全拼
//     *
//     * @param sortKey
//     * @return
//     */
//    public SortToken parseSortKey(String sortKey) {
//        SortToken token = new SortToken();
//        if (sortKey != null && sortKey.length() > 0) {
//            // 其中包含的中文字符
//            String[] enStrs = sortKey.replace(" ", "").split(chReg);
//            for (int i = 0, length = enStrs.length; i < length; i++) {
//                if (enStrs[i].length() > 0) {
//                    // 拼接简拼
//                    token.simpleSpell += enStrs[i].charAt(0);
//                    token.wholeSpell += enStrs[i];
//                }
//            }
//        }
//        return token;
//    }
//
//}
