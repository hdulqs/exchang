import React, { PureComponent } from 'react';
import { Link } from 'dva/router';
import { Icon} from 'antd';
import  BaseAction from '../index';
class BaseForm extends BaseAction {
  state = {
    width: '100%',
    htmlContent: '',
    fileList:null,
  };
  componentDidMount() {
    window.addEventListener('resize', this.resizeFooterToolbar);
  }
  componentWillUnmount() {
    window.removeEventListener('resize', this.resizeFooterToolbar);
  }

  resizeFooterToolbar = () => {
    const sider = document.querySelectorAll('.ant-layout-sider')[0];
    const width = `calc(100% - ${sider.style.width})`;
    if (this.state.width !== width) {
      this.setState({ width });
    }
  }
  /**
   * html 编辑器返回内容
   * @param htmlContent
   */
  handleHTMLChange = (htmlContent) => {
    this.setState({ htmlContent });
  }

  /**
   * 文件上传返回内容
   * @param fileList
   */
  handleFileChange = (fileList) =>{
    this.setState({ fileList });
  }
  toview = (url) =>{
     return (<div>
       <Link to ={url}>
         <Icon type="rollback" />
         返回列表页
       </Link>
     </div>);
  }
}
export default BaseForm;
