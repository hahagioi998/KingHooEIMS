package com.hafele.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月17日 下午4:00:40
* 常亮设置
*/
public class Constants {
	
	// 服务器IP，一般来说就是本机
	public static String SERVER_IP = "127.0.0.1";
	// 服务器port
	public static int SERVER_PORT = 5555;

	// 消息类型
	/** 普通 */
	public static String GENRAL_MSG = "0";
	/** 抖动 */
	public static String SHAKE_MSG = "1";
	/** 回文 */
	public static String PALIND_MSG = "2";
	/** 图片 */
	public static String PICTURE_MSG = "3";
	/** 登录 */
	public static String LOGIN_MSG = "4";
	/** 退出 */
	public static String EXIT_MSG = "5";
	/** 注册 */
	public static String REGISTER_MSG = "6";
	/** 用户信息(修改、查看) */
	public static String INFO_MSG = "7";
	/** 请求添加好友 */
	public static String REQUEST_ADD_MSG = "8";
	/** 回应添加好友 */
	public static String ECHO_ADD_MSG = "9";
	/** 删除分组（user） */
	public static String DELETE_USER_CATE_MSG = "10";
	/** 删除成员（user） */
	public static String DELETE_USER_MEMBER_MSG = "11";
	/** 添加分组（user） */
	public static String ADD_USER_CATE_MSG = "12";
	/** 修改分组（user） */
	public static String EDIT_USER_CATE_MSG = "13";

	public static String USER = "user";
	public static String GROUP = "group";
	public static String SUCCESS = "success";
	public static String FAILURE = "failure";
	public static String YES = "yes";
	public static String NO = "no";

	/** 空格代码 */
	public static String SPACE = "\u0008";
	/** 换行代码 */
	public static String NEWLINE = "\n";
	/** 左斜杠 */
	public static String LEFT_SLASH = "/";

	/** 搜索：联系人、讨论组、群、企业 */
	public static String SEARCH_TXT = "搜索：联系人、讨论组、群、企业";
	/** 联系人 */
	public static String DEFAULT_CATE = "联系人";
	/** 未命名 */
	public static String NONAME_CATE = "未命名";

	/** 白色 */
	public static Border WHITE_BORDER = BorderFactory.createLineBorder(Color.WHITE);
	/** 灰色 */
	public static Border GRAY_BORDER = BorderFactory.createLineBorder(Color.GRAY);
	/** 绿色 */
	public static Border GREEN_BORDER = BorderFactory.createLineBorder(Color.GREEN);
	/** 加亮灰*/
	public static Border LIGHT_GRAY_BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	
	/** 微软雅黑 普通 12号字体 */
	public static Font BASIC_FONT = new Font("微软雅黑", Font.PLAIN, 12);
	/** 微软雅黑 加粗 12号字体 */
	public static Font BASIC_FONT12 = new Font("微软雅黑", Font.TYPE1_FONT, 12);
	/** 微软雅黑 普通 14号字体 */
	public static Font BASIC_FONTT14 = new Font("微软雅黑", Font.PLAIN, 14);
	/** 楷体 */
	public static Font DIALOG_FONT = new Font("楷体", Font.PLAIN, 16);
	/** 联系人、群、讨论组、会话面板背景色 */
	public static Color BACKGROUND_COLOR = new Color(237, 247, 253);
	
	public static int NO_OPTION = 1;
	public static int YES_OPTION = 0;
}
