/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myapp.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 *
 * @author dlugo
 */
public class OperationDTO {

    @Size(max = 16)
    @Column(name = "cedula_beneficiario", length = 16)
    private String cedulaBeneficiario;

    @Size(max = 14)
    @Column(name = "telefono_emisor", length = 14)
    private String telefonoEmisor;

    @Size(max = 14)
    @Column(name = "telefono_beneficiario", length = 14)
    private String telefonoBeneficiario;

    @Size(max = 20)
    @Column(name = "monto", length = 20)
    private String monto;

    @Size(max = 4)
    @Column(name = "banco_emisor", length = 4)
    private String bancoEmisor;

    @Size(max = 255)
    @Column(name = "concepto", length = 255)
    private String concepto;

    @Size(max = 15)
    @Column(name = "referencia", length = 15)
    private String referencia;

    @Size(max = 30)
    @Column(name = "fecha_hora", length = 30)
    private String fechaHora;

    public String getCedulaBeneficiario() {
        return cedulaBeneficiario;
    }

    public void setCedulaBeneficiario(String cedulaBeneficiario) {
        this.cedulaBeneficiario = cedulaBeneficiario;
    }

    public String getTelefonoEmisor() {
        return telefonoEmisor;
    }

    public void setTelefonoEmisor(String telefonoEmisor) {
        this.telefonoEmisor = telefonoEmisor;
    }

    public String getTelefonoBeneficiario() {
        return telefonoBeneficiario;
    }

    public void setTelefonoBeneficiario(String telefonoBeneficiario) {
        this.telefonoBeneficiario = telefonoBeneficiario;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getBancoEmisor() {
        return bancoEmisor;
    }

    public void setBancoEmisor(String bancoEmisor) {
        this.bancoEmisor = bancoEmisor;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        return (
            "OperationDTO{" +
            "cedulaBeneficiario=" +
            cedulaBeneficiario +
            ", telefonoEmisor=" +
            telefonoEmisor +
            ", telefonoBeneficiario=" +
            telefonoBeneficiario +
            ", monto=" +
            monto +
            ", bancoEmisor=" +
            bancoEmisor +
            ", concepto=" +
            concepto +
            ", referencia=" +
            referencia +
            ", fechaHora=" +
            fechaHora +
            '}'
        );
    }
}
