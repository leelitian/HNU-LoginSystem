package util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class PhoneCode {

	/**
	 * �����ƶ��ŷ�������
	 * 
	 * @param mobile
	 * @return
	 */
	public static String getPhonemsg(String code, String mobile) {

		/**
		 * ���������ϵУ��
		 */
		System.out.println(mobile);
		if (mobile == null || mobile == "") {
			System.out.println("�ֻ���Ϊ��");
			return "";
		}
		/**
		 * ������֤---������ڹ���
		 */

		// ���ó�ʱʱ��-�����е���
		System.setProperty(StaticPeram.defaultConnectTimeout, StaticPeram.Timeout);
		System.setProperty(StaticPeram.defaultReadTimeout, StaticPeram.Timeout);
		// ��ʼ��ascClient��Ҫ�ļ�������
		final String product = StaticPeram.product;// ����API��Ʒ���ƣ����Ų�Ʒ���̶��������޸ģ�
		final String domain = StaticPeram.domain;// ����API��Ʒ�������ӿڵ�ַ�̶��������޸ģ�
		// �滻�����AK
		final String accessKeyId = StaticPeram.accessKeyId;// ���accessKeyId,�ο����ĵ�����2
		final String accessKeySecret = StaticPeram.accessKeySecret;// ���accessKeySecret���ο����ĵ�����2
		// ��ʼ��ascClient,��ʱ��֧�ֶ�region
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e1) {
			e1.printStackTrace();
		}

		IAcsClient acsClient = new DefaultAcsClient(profile);
		// ��װ�������
		SendSmsRequest request = new SendSmsRequest();
		// ʹ��post�ύ
		request.setMethod(MethodType.POST);
		// ����:�������ֻ��š�֧���Զ��ŷָ�����ʽ�����������ã���������Ϊ1000���ֻ�����,������������ڵ������ü�ʱ�������ӳ�,��֤�����͵Ķ����Ƽ�ʹ�õ������õķ�ʽ
		request.setPhoneNumbers(mobile);
		// ����:����ǩ��-���ڶ��ſ���̨���ҵ�
		request.setSignName(StaticPeram.SignName);
		// ����:����ģ��-���ڶ��ſ���̨���ҵ�
		request.setTemplateCode(StaticPeram.TemplateCode);
		// ��ѡ:ģ���еı����滻JSON��,��ģ������Ϊ"�װ���${name},������֤��Ϊ${code}"ʱ,�˴���ֵΪ
		// ������ʾ:���JSON����Ҫ�����з�,����ձ�׼��JSONЭ��Ի��з���Ҫ��,������������а���\r\n�������JSON����Ҫ��ʾ��\\r\\n,����ᵼ��JSON�ڷ���˽���ʧ��
		request.setTemplateParam("{\"code\":\"" + code + "\"}");
		System.out.println("{\"code\":\"" + code + "\"}");
		// ��ѡ-���ж�����չ��(�����������û�����Դ��ֶ�)
		// request.setSmsUpExtendCode("90997");
		// ��ѡ:outIdΪ�ṩ��ҵ����չ�ֶ�,�����ڶ��Ż�ִ��Ϣ�н���ֵ���ظ�������
//		request.setOutId("yourOutId");
		// ����ʧ���������ClientException�쳣
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				// ����ɹ�
				System.out.println("��ȡ��֤��ɹ�������");
			} else { // �����֤����󣬻����ԭ��
				System.out.println(sendSmsResponse.getCode());
				return "��������Ƶ����Ŷ��1���Ӻ�����";
			}
		} catch (ServerException e) {
			e.printStackTrace();
			return "����ϵͳά������ʱ�޷�ע�ᣡ����";
		} catch (ClientException e) {
			e.printStackTrace();
			return "����ϵͳά������ʱ�޷�ע�ᣡ����";
		}
		return "��ȡ��֤��ɹ�";
	}
}