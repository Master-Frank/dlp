/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2005-Present Charles Co., Ltd.
 */
import CryptoJS from 'crypto-js';

/**
 * AES-256-ECB对称加密
 * @param text {string} 要加密的明文
 * @param secretKey {string} 密钥，43位随机大小写与数字
 * @returns {string} 加密后的密文，Base64格式
 */
export const aesEcbEncrypt = (text: string, secretKey: string) => {
  const keyHex = CryptoJS.enc.Base64.parse(secretKey);
  const messageHex = CryptoJS.enc.Utf8.parse(text);
  const encrypted = CryptoJS.AES.encrypt(messageHex, keyHex, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  });
  return encrypted.toString();
};

/**
 * AES-256-ECB对称解密
 * @param textBase64 {string} 要解密的密文，Base64格式
 * @param secretKey {string} 密钥，43位随机大小写与数字
 * @returns {string} 解密后的明文
 */
export function aesEcbDecrypt(textBase64: string | CryptoJS.lib.CipherParams, secretKey: string) {
  const keyHex = CryptoJS.enc.Base64.parse(secretKey);
  const decrypt = CryptoJS.AES.decrypt(textBase64, keyHex, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  });
  return CryptoJS.enc.Utf8.stringify(decrypt);
}

/**
 * AES-256-CBC对称加密
 * @param text {string} 要加密的明文
 * @param secretKey {string} 密钥，43位随机大小写与数字
 * @returns {string} 加密后的密文，Base64格式
 */
export function aesBbcEncrypt({ text, secretKey }: { text: any; secretKey: any }) {
  const keyHex = CryptoJS.enc.Base64.parse(secretKey);
  const ivHex = keyHex.clone();
  // 前16字节作为向量
  ivHex.sigBytes = 16;
  ivHex.words.splice(4);
  const messageHex = CryptoJS.enc.Utf8.parse(text);
  const encrypted = CryptoJS.AES.encrypt(messageHex, keyHex, {
    iv: ivHex,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });
  return encrypted.toString();
}

/**
 * AES-256-CBC对称解密
 * @param textBase64 {string} 要解密的密文，Base64格式
 * @param secretKey {string} 密钥，43位随机大小写与数字
 * @returns {string} 解密后的明文
 */
export function aesCbcDecrypt({ textBase64, secretKey }: { textBase64: any; secretKey: any }) {
  const keyHex = CryptoJS.enc.Base64.parse(secretKey);
  const ivHex = keyHex.clone();
  // 前16字节作为向量
  ivHex.sigBytes = 16;
  ivHex.words.splice(4);
  const decrypt = CryptoJS.AES.decrypt(textBase64, keyHex, {
    iv: ivHex,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });
  return CryptoJS.enc.Utf8.stringify(decrypt);
}
