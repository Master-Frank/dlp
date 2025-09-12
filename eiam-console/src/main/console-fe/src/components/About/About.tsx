/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import React from 'react';
import { Col, Divider, Image, Modal, Row, Space, Typography } from 'antd';
import { CopyrightOutlined, InfoCircleFilled } from '@ant-design/icons';
import { FormattedMessage } from '@umijs/max';
import { useSafeState } from 'ahooks';
import { createStyles } from 'antd-style';

const { Text, Link } = Typography;

const useStyle = createStyles({
  main: {
    padding: '10px 0',
  },
});
const About: React.FC = () => {
  const { styles } = useStyle();
  const currentYear = new Date().getFullYear();
  const [aboutOpen, setAboutOpen] = useSafeState(false);

  return (
    <>
      <InfoCircleFilled
        key="InfoCircleFilled"
        onClick={() => {
          setAboutOpen(true);
        }}
      />
      <Modal
        title={<FormattedMessage id={'component.right_content.about.title'} />}
        open={aboutOpen}
        centered
        footer={null}
        width={650}
        onCancel={() => setAboutOpen(false)}
      >
        <div className={styles.main}>
          <Row gutter={16}>
            <Col span={6} style={{ borderRight: '3px solid #f0f2f5' }}>
              <Image src={'/logo.svg'} preview={false} />
              <Divider dashed style={{ marginBottom: 10, marginTop: 10 }} />
              <Image src={'/wecomqrcode.jpg'} preview={false} />
              <Text style={{ textAlign: 'center', display: 'block' }}>企微二维码</Text>
            </Col>
            <Col span={18}>
              <Space direction="vertical" size={'middle'}>
                <Text>产品：DLP 统一登录平台</Text>
                <Text>版本：1.0.0</Text>
                <Text>
                  版权所有 <CopyrightOutlined /> {'Charles有限公司'} 2022-{currentYear}
                  。保留一切权利。
                </Text>
                <Text>
                  警告：本软件受著作权法和国际版权条约的保护，未经授权擅自复制修改分发本程序的全部或任何部分，将要承担一切由此导致的民事或刑事责任。
                </Text>
                <Link href="https://baidu.com" target="_blank">
                  https://baidu.com
                </Link>
              </Space>
            </Col>
          </Row>
        </div>
      </Modal>
    </>
  );
};
export default About;
