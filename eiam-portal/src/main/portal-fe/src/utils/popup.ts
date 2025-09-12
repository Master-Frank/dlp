/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
/**
 * 打开子窗口
 *
 * @param width
 * @param height
 * @param path
 * @param handlerMessage
 */
export const openPopup = (
  path: string,
  handlerMessage: (arg1: MessageEvent, arg2: Window | null) => void,
  width: number = 600,
  height: number = 600,
) => {
  // 计算窗口在屏幕上居中的位置
  const screenLeft = window.screenLeft !== undefined ? window.screenLeft : window.screenX;
  const screenTop = window.screenTop !== undefined ? window.screenTop : window.screenY;
  const innerWidth = window.innerWidth
    ? window.innerWidth
    : document.documentElement.clientWidth
      ? document.documentElement.clientWidth
      : window.screen.width;
  const innerHeight = window.innerHeight
    ? window.innerHeight
    : document.documentElement.clientHeight
      ? document.documentElement.clientHeight
      : window.screen.height;
  const left = (innerWidth - width) / 2 + screenLeft;
  const top = (innerHeight - height) / 2 + screenTop;
  const popup = window.open(
    path,
    'popup',
    `height=${height}, width=${width}, top=${top},left=${left}, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no`,
  );

  // @ts-ignore
  if (typeof window.addEventListener !== 'undefined') {
    window.addEventListener('message', (e) => handlerMessage(e, popup), false);
    // @ts-ignore
  } else if (typeof window.attachEvent !== 'undefined') {
    // @ts-ignore
    window.attachEvent('onmessage', (e) => handlerMessage(e, popup));
  }
};
