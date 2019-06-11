import React, {PureComponent} from 'react';
import {Button, Icon, Upload} from 'antd';
import {message} from "antd/lib/index";
import config from "../../common/config";

class FileUpload extends PureComponent {
  state = {};
  handleChange = (info) => {
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      this.props.callback(info.fileList);
      message.success(`${info.file.name} 上传成功`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} 上传失败`);
    }
  }
  // 该属性必须放到 handleChange 下边handleChange 才会执行  ....
  fileProps = {
    name: 'file',
    //withCredentials:true,
    action: config.apiUrl + '/upload/file',
    onChange: this.handleChange,
  }
  render() {
    return (
      <Upload {...this.fileProps}>
        <Button>
          <Icon type="upload"/> 请选择要上传的文件
        </Button>
      </Upload>
    );
  }
}
export default FileUpload;
